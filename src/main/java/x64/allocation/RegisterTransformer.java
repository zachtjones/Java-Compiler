package x64.allocation;

import helper.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64Context;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;
import x64.pseudo.PseudoInstruction;

import java.util.*;

import static x64.X64InstructionSize.QUAD;
import static x64.allocation.CallingConvention.preservedRegistersNotRBP;
import static x64.operands.X64Register.*;

public class RegisterTransformer {

	@NotNull private List<PseudoInstruction> initialContents;

	/** Holds the last time a register is read. */
	@NotNull private HashMap<X64PseudoRegister, Integer> lastReads;


	/** This is r10 + r11, + the unused argument registers */
	@NotNull private final List<X64Register> tempsAvailable;

	private List<Instruction> results = null;

	/***
	 * Creates a register transformer, used to transform pseudo registers into real ones.
	 * @param contents The contents of the function.
	 * @param context The context to which the function was created.
	 * @param lastLocalUsage The last usages of the local variables.
	 */
	public RegisterTransformer(@NotNull List<PseudoInstruction> contents, @NotNull X64Context context,
							   @NotNull HashMap<X64PseudoRegister, Integer> lastLocalUsage) {
		this.initialContents = contents;
		this.lastReads = lastLocalUsage;

		tempsAvailable = new ArrayList<>(Arrays.asList(CallingConvention.temporaryRegisters()));

		for (int i = context.getHighestArgUsed() + 1; i < CallingConvention.argumentRegisterCount(); i++) {

			tempsAvailable.add(CallingConvention.argumentRegister(i));
		}
	}

	public static class AllocationUnit {
		public final Deque<PseudoInstruction> prologue = new LinkedList<>();
		public final Deque<PseudoInstruction> epilogue = new LinkedList<>();
		@NotNull public final List<Instruction> instructions;

		AllocationUnit(@NotNull List<Instruction> instructions) {
			this.instructions = instructions;
		}
	}

	/**
	 * Allocates the hardware registers,
	 * returning a set of the actual preserved registers that need to be saved / restored.
	 */
	public AllocationUnit allocate() {

		// determine the usages of the registers, as well as which ones are used across function calls
		RegistersUsed usedRegs = new RegistersUsed(lastReads);
		for (int i = 0; i < initialContents.size(); i++) {
			final PseudoInstruction temp = initialContents.get(i);
			temp.markRegisters(i, usedRegs);
			if (temp.isCalling()) {
				usedRegs.markFunctionCall(i);
			}
		}

		// allocate as if there's infinite registers, but want to use as many as needed

		Deque<RegisterMapped> preservedStack = new ArrayDeque<>();
		int maxPreserved = 0; // after the next loop, this holds the number used

		Deque<RegisterMapped> temporaryStack = new ArrayDeque<>();
		int maxTemp = 0;

		HashMap<X64PseudoRegister, RegisterMapped> mapping = new HashMap<>();

		for (int i = 0; i < initialContents.size(); i++) {
			// we can use a register on both operands of the instruction
			// if a preserved register is last used on the current line:
			// - add the native register associated with it back to the stacks
			for (X64PseudoRegister doneWith : usedRegs.getLastReads(i)) {
				// some registers might not actually get mapped to anything
				RegisterMapped doneWithMapped = mapping.get(doneWith);
				if (doneWithMapped != null) {
					if (doneWithMapped.needsPreserved) {
						preservedStack.push(doneWithMapped);
					} else {
						temporaryStack.push(doneWithMapped);
					}
				}
			}

			// if the instruction defines a X64PreservedRegister, then:
			// 1. add it to the map of currentUsed
			// 2. pop from the stack
			for (X64PseudoRegister newUsage : usedRegs.getFirstWrites(i)) {
				if (usedRegs.canBeTemporary(newUsage)) {
					if (temporaryStack.isEmpty()) {
						// allocate a new one
						temporaryStack.push(new RegisterMapped(maxTemp, false));
						maxTemp++;
					}
					mapping.put(newUsage, temporaryStack.pop());
				} else {

					if (preservedStack.isEmpty()) {
						// allocate a new one, first one is 0, second 1, ... maxPreserved is the number allocated
						preservedStack.push(new RegisterMapped(maxPreserved, true));
						maxPreserved++;
					}
					mapping.put(newUsage, preservedStack.pop());
				}
			}
		}

		// calculate if there's enough hardware registers, if there is, the problem is simple:
		//  allocate a register that can be temporary to a temp one if possible, otherwise use a preserved one
		//  allocate a preserved register to a hardware preserved register
		if (hasEnoughHardwareRegs(maxPreserved, maxTemp)) {

			// obtain the mapping to native registers
			Map<X64PseudoRegister, X64Register> nativeMapping = getNatives(maxTemp, mapping);

			// obtain the new list of instructions, with them swapped out
			AllocationContext context = new AllocationContext(nativeMapping, new HashMap<>(), R10, R11);
			List<Instruction> results = new ArrayList<>();
			try {
				for (PseudoInstruction i : initialContents) {
					results.addAll(i.allocate(context));
				}
			} catch (NotSecondScratchException ignored) {} // won't happen

			// determine the function prologue and epilogue
			AllocationUnit au = new AllocationUnit(results);
			X64Register[] preserved = CallingConvention.preservedRegisters();

			int totalPreserved = maxPreserved;
			if (maxTemp > tempsAvailable.size()) {
				totalPreserved += maxTemp - tempsAvailable.size();
			}

			for (int i = 0; i < totalPreserved; i++) {
				au.prologue.add(new PushReg(preserved[i]));
				au.epilogue.addFirst(new PopReg(preserved[i]));
			}
			if (totalPreserved % 2 == 0) {
				// move another 8 bytes to maintains 16 byte alignment on function calls
				au.prologue.add(new SubtractImmToReg(new Immediate(8), RSP, QUAD));
				au.epilogue.addFirst(new AddImmReg(new Immediate(8), RSP, QUAD));
			}

			return au;
		} else {

			// transforms the instructions out, replacing 2 memory op ones with an intermediate calculation
			// here we use the base pointer for that allocation

			// this function also changes the instructions
			int spaceNeeded = rbpTransform(mapping, tempsAvailable, usedRegs);

			// we know we use all the registers, can't use the base pointer elsewhere
			AllocationUnit au = new AllocationUnit(results);
			X64Register[] preservedLeft = CallingConvention.preservedRegistersNotRBP();

			// epilogue is kept in the order by addFirst on all calls
			for (X64Register x64RegisterOperand : preservedLeft) {
				// odd number of these due to number in both conventions
				au.prologue.add(new PushReg(x64RegisterOperand));
				au.epilogue.addFirst(new PopReg(x64RegisterOperand));
			}

			// preserve base pointer & set to the base of the stack frame
			au.prologue.add(new PushReg(RBP)); // stack now has even number pushed
			au.prologue.add(new MoveRegToReg(RSP, RBP, QUAD));

			// spaceNeeded should be oddNumber * 8.
			au.prologue.add(new SubtractImmToReg(new Immediate(spaceNeeded), RSP, QUAD));

			// restore stack and base pointer
			au.epilogue.addFirst(new PopReg(RBP));
			au.epilogue.addFirst(new MoveRegToReg(RBP, RSP, QUAD));

			return au;
		}
	}

	/**
	 * Performs the calculation that there are enough hardware registers for the allocation.
	 * @param numPreserved The number of preserved registers required.
	 * @param numTemporary The number of temporary registers required.
	 * @return A boolean that is if there are enough hardware registers.
	 * Otherwise, the base pointer offsets will be used to store spilled registers
	 */
	private boolean hasEnoughHardwareRegs(int numPreserved, int numTemporary) {

		X64Register[] preservedPossible = CallingConvention.preservedRegisters();

		int totalNumAvailable = tempsAvailable.size() + preservedPossible.length;

		// just need to have enough preserved registers available
		//  and the total number has to be enough for the preserved + temporary (temps can be stored in preserved ones)

		return preservedPossible.length >= numPreserved &&
			totalNumAvailable >= numPreserved + numTemporary;
	}

	/**
	 * Obtains a mapping of the pseudo registers to hardware registers from the mapping with registerMapped.
	 * @param numTemporary The number of temporary registers in the registerMapped map.
	 *                     If there are more than temps available on the list, preserved registers will be mapped to.
	 * @param mapping The mapping of pseudo registers to their register numbers if there is plenty of both types.
	 * @return The valid mapping of pseudo registers to their hardware ones.
	 */
	private Map<X64PseudoRegister, X64Register> getNatives(int numTemporary, Map<X64PseudoRegister, RegisterMapped> mapping) {

		X64Register[] preservedPossible = CallingConvention.preservedRegisters();

		HashMap<X64PseudoRegister, X64Register> nativeMap = new HashMap<>();

		// if more than the temps available, the preserved ones are used
		int preservedBase = numTemporary <= tempsAvailable.size() ? 0 : numTemporary - tempsAvailable.size();

		for (Map.Entry<X64PseudoRegister, RegisterMapped> entry : mapping.entrySet()) {
			X64PseudoRegister key = entry.getKey();
			RegisterMapped value = entry.getValue();

			if (value.needsPreserved) {
				// simple case -- needs preserved, offset by the amount of temps over
				nativeMap.put(key, preservedPossible[value.num + preservedBase]);
			} else if (value.num < tempsAvailable.size()) {
				// fits in the amount of temporaries
				nativeMap.put(key, tempsAvailable.get(value.num));
			} else {
				// requires temporary, not enough, so uses the first few of preserved
				nativeMap.put(key, preservedPossible[value.num - tempsAvailable.size()]);
			}
		}

		return nativeMap;
	}

	/**
	 * transforms the instructions RegisterMapped to native registers and base-pointer offsets.
	 * @param mapping The mapping of allocation if there were infinite registers,
	 *                each being used as many times as possible.
	 * @param tempsAvailable The hardware registers available as temporaries (may contain unused argument registers)
	 * @param usedRegs The registers that are allocated if there's enough registers.
	 * @return The number of bytes of stack space that needs allocated.
	 * This amount will maintain stack alignment.
	 */
	private int rbpTransform(@NotNull HashMap<X64PseudoRegister, RegisterMapped> mapping,
							 @NotNull List<X64Register> tempsAvailable, @NotNull RegistersUsed usedRegs) {

		// todo, loop through counting jumps to increase priority level,
		//  jumps backwards = 3x as much, conditional jumps backwards 2x as much
		// Could also do some prediction based on other heuristics, like forward jumps as well
		// right now, a simple priority incremented by the number of usages

		// now we can determine the which registers are more important, lower important ones get mapped
		//  to base-pointer offsets
		TreeSet<RegisterMapped> priorities = usedRegs.prioritize(mapping);

		LinkedList<X64Register> tempsLeft = new LinkedList<>(tempsAvailable);

		// this particular register is available for transitions of memory to memory to 2 instructions
		final X64Register scratchRegister = tempsLeft.removeFirst();

		try {
			// create a copy of priorities and tempsLeft
			TreeSet<RegisterMapped> copyPriorities = new TreeSet<>(priorities);
			LinkedList<X64Register> copyTempsLeft = new LinkedList<>(tempsLeft);

			return 8 * map(mapping, copyPriorities, copyTempsLeft, scratchRegister, null);

		} catch (NotSecondScratchException e) {
			// need to allocate a second
			X64Register secondScratch = tempsLeft.removeFirst();

			try {
				return 8 * map(mapping, priorities, tempsLeft, scratchRegister, secondScratch);

				// exception shouldn't happen here
			} catch (NotSecondScratchException e2) {
				throw new RuntimeException("There wasn't a second scratch register when there should have been.", e2);
			}
		}


	}

	private int map(@NotNull HashMap<X64PseudoRegister, RegisterMapped> mapping,
					@NotNull TreeSet<RegisterMapped> priorities, @NotNull LinkedList<X64Register> tempsLeft,
					@NotNull X64Register scratchRegister,
					@Nullable X64Register scratchRegister2) throws NotSecondScratchException {

		LinkedList<X64Register> preservedLeft = new LinkedList<>(Arrays.asList(preservedRegistersNotRBP()));

		// allocate them, pulling from the lists until they're empty, then start allocating stack space
		HashMap<RegisterMapped, X64Register> nativeAllocations = new HashMap<>();
		HashMap<RegisterMapped, BPOffset> basePointerOffsets = new HashMap<>();

		RegisterMapped next;
		int stackNumberAllocated = 0;
		while ((next = priorities.pollFirst()) != null) { // this will exhaust priorities queue

			if (!next.needsPreserved && tempsLeft.size() > 0) { // can allocate as temp register
				nativeAllocations.put(next, tempsLeft.removeFirst());

			} else if (preservedLeft.size() > 0) { // can allocate as preserved register
				nativeAllocations.put(next, preservedLeft.removeFirst());

			} else { // ran out, start allocating stack space, first one -8(%rbp)
				stackNumberAllocated++;
				basePointerOffsets.put(next, new BPOffset(8 * -stackNumberAllocated));
			}
		}

		if (stackNumberAllocated % 2 == 0) {
			stackNumberAllocated++;
		}

		HashMap<X64PseudoRegister, X64Register> natives = MapUtils.map(mapping, nativeAllocations);
		HashMap<X64PseudoRegister, BPOffset> locals = MapUtils.map(mapping, basePointerOffsets);
		AllocationContext context = new AllocationContext(natives, locals, scratchRegister, scratchRegister2);

		// Stream<List<Instruction>>
		// 'flattens' the stream of lists into a stream of instructions
		results = new ArrayList<>();
		for (PseudoInstruction i : initialContents) {
			results.addAll(i.allocate(context));
		}

		return stackNumberAllocated;
	}
}


package x64.allocation;

import x64.Instruction;
import x64.X64Context;
import x64.instructions.*;
import x64.operands.*;

import java.util.*;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.allocation.CallingConvention.preservedRegistersNotRBP;
import static x64.operands.X64NativeRegister.RBP;
import static x64.operands.X64NativeRegister.RSP;
import static x64.operands.X64RegisterOperand.of;

public class RegisterTransformer {

	private final ArrayList<Instruction> initialContents;

	/** This is r10 + r11, + the unused argument registers */
	private final List<X64NativeRegister> tempsAvailable;

	/***
	 * Creates a register transformer, used to transform pseudo registers into real ones.
	 * @param contents The contents of the function.
	 * @param context The context to which the function was created.
	 */
	public RegisterTransformer(ArrayList<Instruction> contents, X64Context context) {
		this.initialContents = contents;


		tempsAvailable = new ArrayList<>();
		for (X64RegisterOperand op : CallingConvention.temporaryRegisters()) {
			tempsAvailable.add(op.nativeOne);
		}
		for (int i = context.getHighestArgUsed() + 1; i < CallingConvention.argumentRegisterCount(); i++) {

			tempsAvailable.add(CallingConvention.argumentRegister(i).nativeOne);
		}
	}

	public static class AllocationUnit {
		public final Deque<Instruction> prologue = new LinkedList<>();
		public final Deque<Instruction> epilogue = new LinkedList<>();
	}

	/**
	 * Allocates the hardware registers,
	 * returning a set of the actual preserved registers that need to be saved / restored.
	 */
	public AllocationUnit allocate() {

		// determine the usages of the registers, as well as which ones are used across function calls
		RegistersUsed usedRegs = new RegistersUsed();
		for (int i = 0; i < initialContents.size(); i++) {
			final Instruction temp = initialContents.get(i);
			temp.markRegisters(i, usedRegs);
			if (temp.isCalling()) {
				usedRegs.markFunctionCall(i);
			}
		}

		Map<Integer, X64PreservedRegister> lastUsedLines = usedRegs.getLastUsages();
		Map<Integer, X64PreservedRegister> definedLines = usedRegs.getDefinitions();

		Deque<RegisterMapped> preservedStack = new ArrayDeque<>();
		int maxPreserved = 0; // after the next loop, this holds the number used

		Deque<RegisterMapped> temporaryStack = new ArrayDeque<>();
		int maxTemp = 0;

		Map<X64PreservedRegister, RegisterMapped> mapping = new HashMap<>();

		for (int i = 0; i < initialContents.size(); i++) {
			// we can use a register on both operands of the instruction
			// if a preserved register is last used on the current line:
			// - add the native register associated with it back to the stacks
			if (lastUsedLines.containsKey(i)) {
				final X64PreservedRegister doneWith = lastUsedLines.get(i);

				RegisterMapped doneWithMapped = mapping.get(doneWith);
				if (doneWithMapped.needsPreserved) {
					preservedStack.push(doneWithMapped);
				} else {
					temporaryStack.push(doneWithMapped);
				}
			}

			// if the instruction defines a X64PreservedRegister, then:
			// 1. add it to the map of currentUsed
			// 2. pop from the stack
			if (definedLines.containsKey(i)) {
				final X64PreservedRegister using = definedLines.get(i);

				if (usedRegs.canBeTemporary(using)) {
					if (temporaryStack.isEmpty()) {
						// allocate a new one
						temporaryStack.push(new RegisterMapped(maxTemp, false));
						maxTemp++;
					}
					mapping.put(using, temporaryStack.pop());
				} else {

					if (preservedStack.isEmpty()) {
						// allocate a new one, first one is 0, second 1, ... maxPreserved is the number allocated
						preservedStack.push(new RegisterMapped(maxPreserved, true));
						maxPreserved++;
					}
					mapping.put(using, preservedStack.pop());
				}

			}
		}

		if (hasEnoughHardwareRegs(maxPreserved, maxTemp)) {

			Map<X64PreservedRegister, X64NativeRegister> nativeMapping = getNatives(maxTemp, mapping);


			for (Instruction i : initialContents) {
				i.allocateRegisters(nativeMapping);
			}

			AllocationUnit au = new AllocationUnit();
			X64RegisterOperand[] preserved = CallingConvention.preservedRegisters();

			int totalPreserved = maxPreserved;
			if (maxTemp > tempsAvailable.size()) {
				totalPreserved += maxTemp - tempsAvailable.size();
			}

			for (int i = 0; i < totalPreserved; i++) {
				au.prologue.add(new PushInstruction(preserved[i]));
				au.epilogue.addFirst(new PopInstruction(preserved[i]));
			}
			if (totalPreserved % 2 == 0) {
				// move another 8 bytes to maintains 16 byte alignment on function calls
				au.prologue.add(new SubtractInstruction(new Immediate(8), RSP));
				au.epilogue.addFirst(new AddInstruction(new Immediate(8), RSP));
			}

			return au;
		} else {

			RegisterMapping registerMapping = new RegisterMapping(maxTemp - 1, mapping);

			// we know we use all the registers
			AllocationUnit au = new AllocationUnit();
			X64NativeRegister[] preservedLeft = CallingConvention.preservedRegistersNotRBP();

			for (X64NativeRegister x64RegisterOperand : preservedLeft) { // odd number of these
				au.prologue.add(new PushInstruction(of(x64RegisterOperand)));
				au.epilogue.addFirst(new PopInstruction(of(x64RegisterOperand)));
			}

			au.prologue.add(new PushInstruction(RBP)); // stack now has even number pushed
			au.prologue.add(new MoveInstruction(RSP, RBP));

			// spaceNeeded should be oddNumber * 8.
			//au.prologue.add(new SubtractInstruction(new Immediate(registerMapping.spaceNeeded), RSP));

			au.epilogue.addFirst(new PopInstruction(RBP));
			au.epilogue.addFirst(new MoveInstruction(RBP, RSP));

			// similarly, obtain a mapping, but with certain registers as offset from the rbp
			//   this means we can't use the RBP as another preserved register

			// if there are any instructions that result in 2 memory operands, need to also
			//   keep a temporary reserved for those as well

			// here the allocateRegisters operation will be slightly different,
			//   we'll need to create an interface that covers either: register / rbp-offset memory

			// for simplicity (at least at first), we will allocate 8 bytes for all extra registers

			throw new RuntimeException(mapping.toString());
		}

		// iterate through the instructions, converting the X64PreservedRegister's to the X64NativeRegister's
//		for (Instruction i : initialContents) {
//			i.allocateRegisters(mapping);
//		}
//
//		Set<X64NativeRegister> usedPreservedRegs = new HashSet<>();
//		for (X64NativeRegister reg : mapping.values()) {
//			if (reg != X64NativeRegister.R10.nativeOne && reg != X64NativeRegister.R11.nativeOne) {
//				usedPreservedRegs.add(reg);
//			}
//		}
//
//		// add another one if there are an even number used -- might add one already in the set
//		// since we just need to preserve one, do one of the arguments
//		while (usedPreservedRegs.size() % 2 == 0) {
//			usedPreservedRegs.add(argumentRegister(1).nativeOne);
//		}

//		return usedPreservedRegs;
	}


	private boolean hasEnoughHardwareRegs(int numPreserved, int numTemporary) {

		X64RegisterOperand[] preservedPossible = CallingConvention.preservedRegisters();

		int totalNumAvailable = tempsAvailable.size() + preservedPossible.length;

		// just need to have enough preserved registers available
		//  and the total number has to be enough for the preserved + temporary (temps can be stored in preserved ones)

		return preservedPossible.length >= numPreserved &&
			totalNumAvailable >= numPreserved + numTemporary;
	}

	private Map<X64PreservedRegister, X64NativeRegister> getNatives(int numTemporary, Map<X64PreservedRegister, RegisterMapped> mapping) {

		X64RegisterOperand[] preservedPossible = CallingConvention.preservedRegisters();

		HashMap<X64PreservedRegister, X64NativeRegister> nativeMap = new HashMap<>();

		// if more than the temps available, the preserved ones are used
		int preservedBase = numTemporary <= tempsAvailable.size() ? 0 : numTemporary - tempsAvailable.size();

		for (Map.Entry<X64PreservedRegister, RegisterMapped> entry : mapping.entrySet()) {
			X64PreservedRegister key = entry.getKey();
			RegisterMapped value = entry.getValue();

			if (value.needsPreserved) {
				// simple case -- needs preserved, offset by the amount of temps over
				nativeMap.put(key, preservedPossible[value.num + preservedBase].nativeOne);
			} else if (value.num < tempsAvailable.size()) {
				// fits in the amount of temporaries
				nativeMap.put(key, tempsAvailable.get(value.num));
			} else {
				// requires temporary, not enough, so uses the first few of preserved
				nativeMap.put(key, preservedPossible[value.num - tempsAvailable.size()].nativeOne);
			}
		}

		return nativeMap;
	}

	public class RegisterMapping {

		/** This is the space needed on the stack. Note that this will be (odd number * 8) bytes */
		public final long spaceNeeded;

		RegisterMapping(int numTemporariesRequired, Map<X64PreservedRegister, RegisterMapped> mapping) {

			// todo, loop through counting jumps to increase priority level,
			//  jumps backwards = 3x as much, conditional jumps backwards 2x as much
			// Could also do some prediction based on other heuristics, like forward jumps as well

			// right now, a simple priority incremented by the number of usages
			for (Instruction i : initialContents) {
				 i.prioritizeRegisters(mapping);
			}

			// now we can determine the which registers are more important, lower important ones get mapped
			//  to base-pointer offsets
			TreeSet<RegisterMapped> priorities = new TreeSet<>(mapping.values());

			ArrayList<X64NativeRegister> tempsLeft = new ArrayList<>(tempsAvailable);

			// this particular register is available for transitions of memory to memory to 2 instructions
			X64NativeRegister temporaryIntermediate = tempsLeft.remove(0);

			ArrayList<X64NativeRegister> preservedLeft = new ArrayList<>(Arrays.asList(preservedRegistersNotRBP()));

			// allocate them, pulling from the lists until they're empty, then start allocating stack space
			HashMap<RegisterMapped, X64NativeRegister> nativeAllocations = new HashMap<>();
			HashMap<RegisterMapped, BasePointerOffset> basePointerOffsets = new HashMap<>();
			
			// TODO
			System.out.println("Priorities: " + priorities);
			System.out.println("Temp used as intermediate: " + temporaryIntermediate);
			System.out.println("Temps available list: " + tempsLeft);
			System.out.println("Preserved available list: " + preservedLeft);
		}
	}
}


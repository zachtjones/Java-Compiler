package x64.allocation;

import x64.Instruction;
import x64.X64Context;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.*;

import static x64.allocation.CallingConvention.argumentRegister;

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

	/**
	 * Allocates the hardware registers,
	 * returning a set of the actual preserved registers that need to be saved / restored.
	 */
	public Set<X64NativeRegister> allocate() {

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

			Set<X64NativeRegister> usedPreservedRegs = new HashSet<>();
			for (X64NativeRegister reg : nativeMapping.values()) {
				if (reg != X64NativeRegister.R10.nativeOne && reg != X64NativeRegister.R11.nativeOne) {
					usedPreservedRegs.add(reg);
				}
			}

			// add another one if there are an even number used -- might add one already in the set
			// since we just need to preserve one, do one of the arguments
			while (usedPreservedRegs.size() % 2 == 0) {
				usedPreservedRegs.add(argumentRegister(1).nativeOne);
			}

			return usedPreservedRegs;
		} else {
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
			} else {
				if (value.num < tempsAvailable.size()) {
					// fits in the amount of temporaries
					nativeMap.put(key, tempsAvailable.get(value.num));
				} else {
					// requires temporary, not enough, so uses the first few of preserved
					nativeMap.put(key, preservedPossible[value.num - tempsAvailable.size()].nativeOne);
				}
			}
		}

		return nativeMap;
	}
}


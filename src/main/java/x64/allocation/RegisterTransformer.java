package x64.allocation;

import x64.Instruction;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.*;

public class RegisterTransformer {

	private final ArrayList<Instruction> initialContents;
	private final Deque<X64NativeRegister> preservedOnes;
	private final Deque<X64NativeRegister> temporaryOnes;

	private final Deque<X64NativeRegister> initialTemps;

	public RegisterTransformer(ArrayList<Instruction> contents) {
		this.initialContents = contents;

		preservedOnes = new ArrayDeque<>();
		for (X64RegisterOperand op : CallingConvention.preservedRegisters()) {
			preservedOnes.add(op.nativeOne);
		}

		temporaryOnes = new ArrayDeque<>();
		initialTemps = new ArrayDeque<>();
		for (X64RegisterOperand op : CallingConvention.temporaryRegisters()) {
			temporaryOnes.add(op.nativeOne);
			initialTemps.add(op.nativeOne);
		}
	}

	private X64NativeRegister getNextTemporary() {
		if (!temporaryOnes.isEmpty()) return temporaryOnes.pop();
		return preservedOnes.pop();
	}

	private X64NativeRegister getNextPreserved() {
		return preservedOnes.pop();
	}

	private void doneWithRegister(X64NativeRegister reg) {
		if (initialTemps.contains(reg)) {
			temporaryOnes.push(reg);
		} else {
			preservedOnes.push(reg);
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

		Map<X64PreservedRegister, X64NativeRegister> mapping = new HashMap<>();

		for (int i = 0; i < initialContents.size(); i++) {
			// if the instruction defines a X64PreservedRegister, then:
			// 1. add it to the map of currentUsed
			// 2. pop from the stack
			if (definedLines.containsKey(i)) {
				final X64PreservedRegister using = definedLines.get(i);

				X64NativeRegister replacement =
					usedRegs.canBeTemporary(using) ? getNextTemporary() : getNextPreserved();
				mapping.put(using, replacement);
			}

			// if a preserved register is last used on the current line:
			// - add the native register associated with it back to the stacks
			if (lastUsedLines.containsKey(i)) {
				final X64PreservedRegister doneWith = lastUsedLines.get(i);

				doneWithRegister(mapping.get(doneWith));
			}
		}

		// iterate through the instructions, converting the X64PreservedRegister's to the X64NativeRegister's
		for (Instruction i : initialContents) {
			i.allocateRegisters(mapping);
		}

		Set<X64NativeRegister> usedPreservedRegs = new HashSet<>();
		for (X64NativeRegister reg : mapping.values()) {
			if (reg != X64NativeRegister.R10.nativeOne && reg != X64NativeRegister.R11.nativeOne) {
				usedPreservedRegs.add(reg);
			}
		}

		return usedPreservedRegs;
	}

}

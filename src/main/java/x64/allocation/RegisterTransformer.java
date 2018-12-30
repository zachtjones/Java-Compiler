package x64.allocation;

import x64.Instruction;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RegisterTransformer {

	private final ArrayList<Instruction> initialContents;
	private final Stack<X64NativeRegister> preservedOnes;
	private final Stack<X64NativeRegister> temporaryOnes;

	public RegisterTransformer(ArrayList<Instruction> contents) {
		this.initialContents = contents;

		preservedOnes = new Stack<>();
		preservedOnes.push(X64NativeRegister.RBX.nativeOne);
		preservedOnes.push(X64NativeRegister.RBP.nativeOne);
		preservedOnes.push(X64NativeRegister.R12.nativeOne);
		preservedOnes.push(X64NativeRegister.R13.nativeOne);
		preservedOnes.push(X64NativeRegister.R14.nativeOne);
		preservedOnes.push(X64NativeRegister.R15.nativeOne);

		temporaryOnes = new Stack<>();
		temporaryOnes.push(X64NativeRegister.R10.nativeOne);
		temporaryOnes.push(X64NativeRegister.R11.nativeOne);
	}

	private X64NativeRegister getNextTemporary() {
		if (!temporaryOnes.empty()) return temporaryOnes.pop();
		return preservedOnes.pop();
	}

	private X64NativeRegister getNextPreserved() {
		return preservedOnes.pop();
	}

	private void doneWithRegister(X64NativeRegister reg) {
		if (reg == X64NativeRegister.R10.nativeOne || reg == X64NativeRegister.R11.nativeOne) {
			temporaryOnes.push(reg);
		} else {
			preservedOnes.push(reg);
		}
	}

	public AllocatedUnit allocate() {

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

		AllocatedUnit au = new AllocatedUnit();
		au.afterAllocationInstructions = initialContents;
		return au;
	}

}

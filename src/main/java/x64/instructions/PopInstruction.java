package x64.instructions;

import x64.Instruction;
import x64.operands.X64RegisterOperand;

public class PopInstruction implements Instruction {

	private X64RegisterOperand reg;

	public PopInstruction(X64RegisterOperand reg) {
		this.reg = reg;
	}

	@Override
	public String toString() {
		return "\tpopq " + reg;
	}
}

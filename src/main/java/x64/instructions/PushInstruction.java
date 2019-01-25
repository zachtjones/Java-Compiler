package x64.instructions;

import x64.Instruction;
import x64.operands.X64RegisterOperand;

public class PushInstruction implements Instruction {

	private X64RegisterOperand reg;

	public PushInstruction(X64RegisterOperand reg) {
		this.reg = reg;
	}

	@Override
	public String toString() {
		return "\tpushq " + reg;
	}
}

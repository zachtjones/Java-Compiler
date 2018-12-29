package x64.instructions;

import x64.Instruction;
import x64.operands.X64NativeRegister;

public class PopInstruction implements Instruction {

	private X64NativeRegister reg;

	public PopInstruction(X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public String toString() {
		return "\tpopq " + reg;
	}
}

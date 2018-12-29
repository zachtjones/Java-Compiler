package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;

public class PopInstruction implements Instruction {

	private X64NativeRegister reg;

	public PopInstruction(X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public boolean isCalling() {
		return false;
	}

	// Although this does use a register, this one isn't of significance
	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {}

	@Override
	public String toString() {
		return "\tpopq " + reg;
	}
}

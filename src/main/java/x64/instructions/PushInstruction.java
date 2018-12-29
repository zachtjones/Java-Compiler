package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;

public class PushInstruction implements Instruction {

	private X64NativeRegister reg;

	public PushInstruction(X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public boolean isCalling() {
		return false;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		// although this uses a register, this is only used to preserve the register
	}

	@Override
	public String toString() {
		return "\tpushq " + reg;
	}
}

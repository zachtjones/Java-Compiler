package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.Map;

public class PushInstruction implements Instruction {

	private X64RegisterOperand reg;

	public PushInstruction(X64RegisterOperand reg) {
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
	public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
		// same reasoning as markRegisters
	}

	@Override
	public String toString() {
		return "\tpushq " + reg;
	}
}

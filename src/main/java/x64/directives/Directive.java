package x64.directives;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.allocation.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

public abstract class Directive implements Instruction {


	@Override
	public boolean isCalling() {
		return false;
	}

	// no registers used
	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {}

	// no register used
	@Override
	public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {}
}

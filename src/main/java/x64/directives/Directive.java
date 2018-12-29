package x64.directives;

import x64.Instruction;
import x64.allocation.RegistersUsed;

public abstract class Directive implements Instruction {


	@Override
	public boolean isCalling() {
		return false;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {

	}
}

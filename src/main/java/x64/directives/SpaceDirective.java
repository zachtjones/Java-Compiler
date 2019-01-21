package x64.directives;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

public class SpaceDirective implements Instruction {
	private int size;

	public SpaceDirective(int size) {
		this.size = size;
	}

	@Override
	public boolean isCalling() {
		return false;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		// nothing needed
	}

	@Override
	public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
		// no swapping needed
	}

	@Override
	public String toString() {
		return ".space " + size;
	}
}

package x64.operands;

import x64.X64InstructionSize;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

public class BasePointerOffset implements Operand {

	private final int offset;

	public BasePointerOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public X64InstructionSize getSuffix() {
		return X64InstructionSize.QUAD;
	}

	@Override
	public void markDefined(int i, RegistersUsed usedRegs) {
		// uses the %rbp as the register
	}

	@Override
	public void markUsed(int i, RegistersUsed usedRegs) {
		// uses the %rbp as the register
	}

	@Override
	public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
		// won't be called
	}

	@Override
	public String toString() {
		return offset + "(%rbp)";
	}
}

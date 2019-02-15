package x64.operands;

import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

public class BasePointerOffset implements Operand {

	private final int offset;

	public BasePointerOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public Instruction.Size getSuffix() {
		return Instruction.Size.QUAD;
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
	public void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping) {
		// won't be called
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

package x64.operands;

import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

/**
 * This class represents the addressable registers for the x64 register set.
 * The only register that exists that is not a part of this set is the instruction pointer,
 *   which can only be used indirectly as an offset.
 */
public enum X64NativeRegister implements Operand {
	RAX("rax"), RBX("rbx"), RCX("rcx"), RDX("rdx"),
	RDI("rdi"), RSI("rsi"), RBP("rbp"), RSP("rsp"),
	R8("r8"),   R9("r9"),   R10("r10"), R11("r11"),
	R12("r12"), R13("r13"), R14("r14"), R15("r15");


	/** How this x64 machine register is represented in the assembly language*/
	private final String representation;

	X64NativeRegister(String representation) {
		this.representation = "%" + representation;
	}

	public Instruction.Size getSuffix() {
		return Instruction.Size.QUAD;
	}

	public String assemblyRep() {
		return representation;
	}

	@Override
	public String toString() {
		return assemblyRep();
	}

	/* These next 3 functions only deal with the preserved registers */

	@Override
	public void markUsed(int i, RegistersUsed usedRegs) {}

	@Override
	public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {}

	@Override
	public void markDefined(int i, RegistersUsed usedRegs) {}
}

package x64.operands;

import x64.Instruction;

import static x64.operands.X64RegisterOperand.of;

/**
 * This class represents the addressable registers for the native x64 register set.
 */
public class X64NativeRegister {

	/** Represents the first argument register */
	public static final X64RegisterOperand RDI = of(new X64NativeRegister("%rdi"));
	/** Represents the second argument register */
	public static final X64RegisterOperand RSI = of(new X64NativeRegister("%rsi"));
	/** Represents the third argument register */
	public static final X64RegisterOperand RDX = of(new X64NativeRegister("%rdx"));
	/** Represents the fourth argument register */
	public static final X64RegisterOperand RCX = of(new X64NativeRegister("%rcx"));
	/** Represents the fifth argument register */
	public static final X64RegisterOperand R8 = of(new X64NativeRegister("%r8"));
	/** Represents the sixth argument register */
	public static final X64RegisterOperand R9 = of(new X64NativeRegister("%r9"));

	/** Represents the return register */
	public static final X64RegisterOperand RAX = of(new X64NativeRegister("%rax"));

	// preserved registers
	public static final X64RegisterOperand RBX = of(new X64NativeRegister("%rbx"));
	public static final X64RegisterOperand RBP = of(new X64NativeRegister("%rbp"));
	public static final X64RegisterOperand R12 = of(new X64NativeRegister("%r12"));
	public static final X64RegisterOperand R13 = of(new X64NativeRegister("%r13"));
	public static final X64RegisterOperand R14 = of(new X64NativeRegister("%r14"));
	public static final X64RegisterOperand R15 = of(new X64NativeRegister("%r15"));

	// temporary ones
	public static final X64RegisterOperand R10 = of(new X64NativeRegister("%r10"));
	public static final X64RegisterOperand R11 = of(new X64NativeRegister("%r11"));

	// stack pointer
	public static final X64RegisterOperand RSP = of(new X64NativeRegister("%rsp"));

	/** How this x64 machine register is represented in the assembly language*/
	private final String representation;

	private X64NativeRegister(String representation) {
		this.representation = representation;
	}

	public Instruction.Size getSuffix() {
		return Instruction.Size.QUAD;
	}

	public String assemblyRep() {
		return representation;
	}

}

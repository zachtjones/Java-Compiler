package x64.operands;

import x64.Instruction;

import static x64.Instruction.Size.QUAD;

/**
 * This class represents the addressable registers for the native x64 register set.
 */
public class X64NativeRegister extends X64PreservedRegister {

	/** Represents the first argument register */
	public static final X64NativeRegister RDI = new X64NativeRegister(QUAD, "%rdi");
	/** Represents the second argument register */
	public static final X64NativeRegister RSI = new X64NativeRegister(QUAD, "%rsi");
	/** Represents the third argument register */
	public static final X64NativeRegister RDX = new X64NativeRegister(QUAD, "%rdx");
	/** Represents the fourth argument register */
	public static final X64NativeRegister RCX = new X64NativeRegister(QUAD, "%rcx");
	/** Represents the fifth argument register */
	public static final X64NativeRegister R8 = new X64NativeRegister(QUAD, "%r8");
	/** Represents the sixth argument register */
	public static final X64NativeRegister R9 = new X64NativeRegister(QUAD, "%r9");

	/** Represents the return register */
	public static final X64NativeRegister RAX = new X64NativeRegister(QUAD, "%rax");

	public static final X64NativeRegister RBX = new X64NativeRegister(QUAD, "%rbx");
	public static final X64NativeRegister RBP = new X64NativeRegister(QUAD, "%rbp");
	public static final X64NativeRegister R12 = new X64NativeRegister(QUAD, "%r12");
	public static final X64NativeRegister R13 = new X64NativeRegister(QUAD, "%r13");
	public static final X64NativeRegister R14 = new X64NativeRegister(QUAD, "%r14");
	public static final X64NativeRegister R15 = new X64NativeRegister(QUAD, "%r15");

	/** How this x64 machine register is represented in the assembly language*/
	private final String representation;

	private X64NativeRegister(Instruction.Size size, String representation) {
		super(-1, size);
		this.representation = representation;
	}

	@Override
	public String assemblyRep() {
		return representation;
	}
}

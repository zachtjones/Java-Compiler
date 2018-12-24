package x64;

import static x64.Instruction.Size.QUAD;

/**
 * This class represents the addressable registers for the native x64 register set.
 */
public class X64NativeRegister extends X64Register {

	public static final X64NativeRegister RDI = new X64NativeRegister(ARGUMENT, QUAD, "%rdi");
	public static final X64NativeRegister RSI = new X64NativeRegister(ARGUMENT, QUAD, "%rsi");
	public static final X64NativeRegister RDX = new X64NativeRegister(ARGUMENT, QUAD, "%rdx");
	public static final X64NativeRegister RCX = new X64NativeRegister(ARGUMENT, QUAD, "%rcx");
	public static final X64NativeRegister R8 = new X64NativeRegister(ARGUMENT, QUAD, "%r8");
	public static final X64NativeRegister R9 = new X64NativeRegister(ARGUMENT, QUAD, "%r9");

	public static final X64NativeRegister RAX = new X64NativeRegister("r", QUAD, "%rax");

	public static final X64NativeRegister RBX = new X64NativeRegister(ARGUMENT, QUAD, "%rbx");
	public static final X64NativeRegister RBP = new X64NativeRegister(ARGUMENT, QUAD, "%rbp");
	public static final X64NativeRegister R12 = new X64NativeRegister(ARGUMENT, QUAD, "%r12");
	public static final X64NativeRegister R13 = new X64NativeRegister(ARGUMENT, QUAD, "%r13");
	public static final X64NativeRegister R14 = new X64NativeRegister(ARGUMENT, QUAD, "%r14");
	public static final X64NativeRegister R15 = new X64NativeRegister(ARGUMENT, QUAD, "%r15");

	/** How this x64 machine register is represented in the assembly language*/
	private final String representation;

	private X64NativeRegister(String type, Instruction.Size size, String representation) {
		super(-1, type, size);
		this.representation = representation;
	}

	@Override
	public String assemblyRep() {
		return representation;
	}
}

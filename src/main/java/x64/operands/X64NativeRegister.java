package x64.operands;

import x64.Instruction;

/**
 * This class represents the addressable registers for the native x64 register set.
 */
public class X64NativeRegister implements SourceOperand, DestinationOperand {

	/** Represents the first argument register */
	public static final X64NativeRegister RDI = new X64NativeRegister("%rdi");
	/** Represents the second argument register */
	public static final X64NativeRegister RSI = new X64NativeRegister("%rsi");
	/** Represents the third argument register */
	public static final X64NativeRegister RDX = new X64NativeRegister("%rdx");
	/** Represents the fourth argument register */
	public static final X64NativeRegister RCX = new X64NativeRegister("%rcx");
	/** Represents the fifth argument register */
	public static final X64NativeRegister R8 = new X64NativeRegister("%r8");
	/** Represents the sixth argument register */
	public static final X64NativeRegister R9 = new X64NativeRegister("%r9");

	/** Represents the return register */
	public static final X64NativeRegister RAX = new X64NativeRegister("%rax");

	public static final X64NativeRegister RBX = new X64NativeRegister("%rbx");
	public static final X64NativeRegister RBP = new X64NativeRegister("%rbp");
	public static final X64NativeRegister R12 = new X64NativeRegister("%r12");
	public static final X64NativeRegister R13 = new X64NativeRegister("%r13");
	public static final X64NativeRegister R14 = new X64NativeRegister("%r14");
	public static final X64NativeRegister R15 = new X64NativeRegister("%r15");

	private static final X64NativeRegister[] args = { RDI, RSI, RDX, RCX, R8, R9 };

	/** How this x64 machine register is represented in the assembly language*/
	private final String representation;

	private X64NativeRegister(String representation) {
		this.representation = representation;
	}

	/** Returns the argument numbered, starting from 1 as %rdi */
	public static X64NativeRegister argNumbered(int paramIndex) {
		return args[paramIndex - 1];
	}

	@Override
	public Instruction.Size getSuffix() {
		return Instruction.Size.QUAD;
	}

	@Override
	public String assemblyRep() {
		return representation;
	}
}

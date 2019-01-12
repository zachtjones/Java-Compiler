package x64.allocation;

import x64.Instruction;
import x64.operands.X64RegisterOperand;

import static x64.operands.X64RegisterOperand.of;

/**
 * This class represents the addressable registers for the native x64 register set.
 */
public class X64NativeRegister {

	/** Represents the first argument register */
	static final X64RegisterOperand RDI = of(new X64NativeRegister("%rdi"));
	/** Represents the second argument register */
	static final X64RegisterOperand RSI = of(new X64NativeRegister("%rsi"));
	/** Represents the third argument register */
	static final X64RegisterOperand RDX = of(new X64NativeRegister("%rdx"));
	/** Represents the fourth argument register */
	static final X64RegisterOperand RCX = of(new X64NativeRegister("%rcx"));
	/** Represents the fifth argument register */
	static final X64RegisterOperand R8 = of(new X64NativeRegister("%r8"));
	/** Represents the sixth argument register */
	static final X64RegisterOperand R9 = of(new X64NativeRegister("%r9"));

	/** Represents the return register */
	static final X64RegisterOperand RAX = of(new X64NativeRegister("%rax"));

	// preserved registers
	static final X64RegisterOperand RBX = of(new X64NativeRegister("%rbx"));
	static final X64RegisterOperand RBP = of(new X64NativeRegister("%rbp"));
	static final X64RegisterOperand R12 = of(new X64NativeRegister("%r12"));
	static final X64RegisterOperand R13 = of(new X64NativeRegister("%r13"));
	static final X64RegisterOperand R14 = of(new X64NativeRegister("%r14"));
	static final X64RegisterOperand R15 = of(new X64NativeRegister("%r15"));

	// temporary ones
	static final X64RegisterOperand R10 = of(new X64NativeRegister("%r10"));
	static final X64RegisterOperand R11 = of(new X64NativeRegister("%r11"));

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

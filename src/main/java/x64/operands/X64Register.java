package x64.operands;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;

/**
 * This class represents the addressable registers for the x64 register set.
 * The only register that exists that is not a part of this set is the instruction pointer,
 *   which can only be used indirectly as an offset.
 */
public enum X64Register {
	RAX("rax", "eax", "ax", "al"),
	RBX("rbx", "ebx", "bx", "bl"),
	RCX("rcx", "ecx", "cx", "cl"),
	RDX("rdx", "edx", "dx", "dl"),
	RDI("rdi", "edi", "di", "dil"),
	RSI("rsi", "esi", "si", "sil"),
	RBP("rbp", "ebp", "bp", "bpl"),
	RSP("rsp", "esp", "sp", "spl"),
	R8 ("r8",  "r8d",  "r8w",  "r8b"),
	R9 ("r9",  "r9d",  "r9w",  "r9b"),
	R10("r10", "r10d", "r10w", "r10b"),
	R11("r11", "r11d", "r11w", "r11b"),
	R12("r12", "r12d", "r12w", "r12b"),
	R13("r13", "r13d", "r13w", "r13b"),
	R14("r14", "r14d", "r14w", "r14b"),
	R15("r15", "r15d", "r15w", "r15b");


	/** How this x64 machine register is represented in the assembly language*/
	@NotNull private final String quadSize, doubleSize, wordSize, byteSize;

	X64Register(String quadSize, String doubleSize, String wordSize, String byteSize) {
		this.quadSize = "%" + quadSize;
		this.doubleSize = "%" + doubleSize;
		this.wordSize = "%" + wordSize;
		this.byteSize = "%" + byteSize;
	}

	public X64InstructionSize getSuffix() {
		return X64InstructionSize.QUAD;
	}

	/** Returns the representation as a quad word */
	public String assemblyRep() {
		return quadSize;
	}

	/** Returns the representation for the size specified. */
	public String assemblyRep(X64InstructionSize size) {
		switch (size) {
			case BYTE:
				return byteSize;
			case WORD:
				return wordSize;
			case LONG:
				return doubleSize;
			case QUAD:
				return quadSize;
			default:
				throw new RuntimeException("x64 general purpose register used for floating point size.");
		}
	}

	/** Returns the representation as a byte sized register */
	public String byteSizeRep() {
		return byteSize;
	}

	@Override
	public String toString() {
		return assemblyRep();
	}
}

package x64.allocation;

import x64.operands.X64RegisterOperand;

import static x64.operands.X64NativeRegister.*;

/**
 * This class deals with the platform specifics of the calling conventions, allowing the other classes to refer
 * to the registers as arg4 instead of RCX on System V AMD64 or R9 on Microsoft x64
 */
public class CallingConvention {

	/** holds if this system is a Microsoft system, so that we don't need repeated system property lookups */
	public static final boolean isMicrosoft = System.getProperty("os.name").contains("Windows");
	/** holds if this system is a Mac OS system, so that we don't need repeated system property lookups */
	public static boolean isMac = System.getProperty("os.name").contains("Mac");

	private static final X64RegisterOperand[] argsSystemV = { RDI, RSI, RDX, RCX, R8, R9 };
	private static final X64RegisterOperand[] argsMicrosoft = { RCX, RDX, R8, R9 };

	private static final X64RegisterOperand[] preservedSystemV = { RBX, RBP, R12, R13, R14, R15 };
	private static final X64RegisterOperand[] preservedMicrosoft = { RBX, RBP, RDI, RSI, R12, R13, R14, R15 };

	private static final X64RegisterOperand[] extraTemps = { R10, R11 };


	/**
	 * Returns the x64 native register that is mapped to that argument.
	 * @param num 1 for the first argument, 2 for the second, ...
	 * @return The RegisterOperand which is a reference to that register.
	 */
	public static X64RegisterOperand argumentRegister(int num) {
		// TODO a sort of pseudo-register for extra arguments more than 4 / 6
		if (isMicrosoft) {
			return argsMicrosoft[num - 1];
		} else {
			return argsSystemV[num - 1];
		}
	}

	/** returns the register operand that is used for holding return values */
	public static X64RegisterOperand returnValueRegister() {
		// both System V and Microsoft are same
		return RAX;
	}

	/** returns the array of registers whose values must be preserved (not including the stack pointer) */
	static X64RegisterOperand[] preservedRegisters() {
		return isMicrosoft ? preservedMicrosoft : preservedSystemV;
	}

	/** returns the array of registers whose values are temporary */
	static X64RegisterOperand[] temporaryRegisters() {
		return extraTemps;
	}

	/** returns true if the caller need to allocate 32 bytes (4 registers) for the callee to store it's args */
	public static boolean needsToAllocate32BytesForArgs() {
		return isMicrosoft;
	}

	/** returns the string used in the call instruction for a c library function*/
	public static String libraryFunc(String function) {
		return (isMicrosoft ? "" : "_") + function;
	}
}

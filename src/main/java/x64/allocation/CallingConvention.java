package x64.allocation;

import x64.operands.X64RegisterOperand;

import static x64.operands.X64NativeRegister.*;

/**
 * This represents the calling convention, including useful methods for abstracting away the current
 * platform's calling convention.
 * This class deals with the platform specifics of the calling conventions, allowing the other classes to refer
 * to the registers as arg4 instead of RCX on System V AMD64 or R9 on Microsoft x64
 */
public class CallingConvention {

	private final boolean isMicrosoft;

	/** Uses a constructor & non-static methods so that system properties don't need to be looked up a lot. */
	public CallingConvention() {
		isMicrosoft = systemIsMicrosoft();
	}

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
	public X64RegisterOperand getArgumentRegister(int num) {
		// TODO a sort of pseudo-register for extra arguments more than 4 / 6
		if (isMicrosoft) {
			return argsMicrosoft[num - 1];
		} else {
			return argsSystemV[num - 1];
		}
	}

	/** returns the register operand that is used for holding return values */
	public X64RegisterOperand getReturnValueRegister() {
		// both System V and Microsoft are same
		return RAX;
	}

	/** returns the array of registers whose values must be preserved (not including the stack pointer) */
	public X64RegisterOperand[] getPreservedRegisters() {
		return isMicrosoft ? preservedMicrosoft : preservedSystemV;
	}

	/** returns the array of registers whose values are temporary */
	public X64RegisterOperand[] getTemporaryRegisters() {
		return extraTemps;
	}

	/** Utility method to determine if the system uses the microsoft calling convention. */
	private static boolean systemIsMicrosoft() {
		return System.getProperty("os.name").contains("Windows");
	}
}

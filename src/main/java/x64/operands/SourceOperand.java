package x64.operands;

import x64.allocation.RegistersUsed;

import java.util.Map;

/**
 * Represents an x64 source operand, which can be either a register, memory combo, or immediate
 */
public interface SourceOperand {

    /** How this source operand should be written in x64 */
    String toString();

    /** Call markUsed on the usedRegs parameter if the source of this is based on a register */
	void markUsed(int i, RegistersUsed usedRegs);

	/** Swaps the preserved registers with their allocated real registers */
	void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping);
}

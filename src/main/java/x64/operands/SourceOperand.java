package x64.operands;

import x64.allocation.RegistersUsed;

/**
 * Represents an x64 source operand, which can be either a register, memory combo, or immediate
 */
public interface SourceOperand {

    /** How this source operand should be written in x64 */
    String assemblyRep();

    /** Call markUsed on the usedRegs parameter if the source of this is based on a register */
	void markUsed(int i, RegistersUsed usedRegs);
}

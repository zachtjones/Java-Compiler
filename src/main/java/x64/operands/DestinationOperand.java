package x64.operands;

import x64.Instruction;
import x64.allocation.RegistersUsed;

/**
 * Represents an x64 destination operand, which can be either a register or memory combo
 */
public interface DestinationOperand {

    /** The instruction size that should be selected to use this destination operand. */
    Instruction.Size getSuffix();

    /** How this source operand should be written in x64 */
    String assemblyRep();

    /** call markDefined on the used register argument if this uses a register. */
	void markDefined(int i, RegistersUsed usedRegs);
}

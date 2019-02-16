package x64.operands;

import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

/**
 * Represents an x64 destination operand, which can be either a register or memory combo
 */
public interface DestinationOperand {

    /** The instruction size that should be selected to use this destination operand. */
    Instruction.Size getSuffix();

    /** How this source operand should be written in x64 */
    String toString();

    /** call markDefined on the used register argument if this uses a register. */
	void markDefined(int i, RegistersUsed usedRegs);

    /** Increments the priority of the allocated register when it is used.
     * @param mapping The RegisterMapped instance that each pseudo register is mapped to */
    void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping);
}

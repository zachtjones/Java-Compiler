package x64;

/**
 * Represents an x64 destination operand, which can be either a register or memory combo
 */
public interface DestinationOperand {

    /** The instruction size that should be selected to use this destination operand. */
    Instruction.Size getSuffix();

    /** How this source operand should be written in x64 */
    String assemblyRep();
}

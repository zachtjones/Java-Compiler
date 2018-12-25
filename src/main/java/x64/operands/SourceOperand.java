package x64.operands;

/**
 * Represents an x64 source operand, which can be either a register, memory combo, or immediate
 */
public interface SourceOperand {

    /** How this source operand should be written in x64 */
    String assemblyRep();
}

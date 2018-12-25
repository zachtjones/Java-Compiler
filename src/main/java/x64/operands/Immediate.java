package x64.operands;

/** Represents an immediate value that can be directly part of the instruction.
 * The assembler chooses which size of data is necessary for the instruction, but up to 64-bit values are allowed */
public class Immediate implements SourceOperand {

    private long value;

    /** Creates an immediate constant value from the literal value.
     * Note that all values don't require the full 64-bit representation,
     * the assembler will optimize instruction size. */
    public Immediate(long value) {
        this.value = value;
    }


    @Override
    public String assemblyRep() {
        // simple toString
        return Long.toString(value);
    }
}

package x64.instructions;

import x64.Instruction;

/** Represents the simple return instruction, which pops the return address & jumps back. */
public class ReturnInstruction implements Instruction {

    /** represents the instance of a return instruction.
     * Since all are identical, there's no need to create a bunch*/
    public final static ReturnInstruction instance = new ReturnInstruction();

    /** prevent creation outside this class */
    private ReturnInstruction() {}

    @Override
    public String toString() {
        return "\tret";
    }
}

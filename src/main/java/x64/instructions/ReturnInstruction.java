package x64.instructions;

import x64.Instruction;

/** Represents the simple return instruction, which pops the return address & jumps back. */
public class ReturnInstruction implements Instruction {
    @Override
    public String toString() {
        return "\tretq";
    }
}

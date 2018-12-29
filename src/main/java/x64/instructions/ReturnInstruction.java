package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;

/** Represents the simple return instruction, which pops the return address & jumps back. */
public class ReturnInstruction implements Instruction {

    @Override
    public boolean isCalling() {
        return false;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        // doesn't use registers
    }

    @Override
    public String toString() {
        return "\tretq";
    }
}

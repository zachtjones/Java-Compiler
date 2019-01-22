package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

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
    public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        // same reasoning as markRegisters
    }

    @Override
    public String toString() {
        return "\tret";
    }
}

package x64.operands;

import x64.Instruction;
import x64.allocation.RegistersUsed;

import java.util.Map;

/** Represents a memory displacement from a register with a known constant integer offset */
public class RegisterRelativePointer implements SourceOperand, DestinationOperand {

    private final int offset;
    private X64RegisterOperand register;

    public RegisterRelativePointer(int offset, X64RegisterOperand register) {
        this.offset = offset;
        this.register = register;
    }

    @Override
    public Instruction.Size getSuffix() {
        return Instruction.Size.QUAD;
    }

    @Override
    public String toString() {
        return offset + "(" + register.toString() + ")";
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        register.markUsed(i, usedRegs);
    }

    @Override
    public void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        register.swapOut(mapping);
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        register.markDefined(i, usedRegs);
    }
}

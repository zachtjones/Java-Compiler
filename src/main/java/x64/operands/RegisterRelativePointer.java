package x64.operands;

import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

/** Represents a memory displacement from a register with a known constant integer offset */
public class RegisterRelativePointer implements Operand {

    private final int offset;
    private X64PreservedRegister register;

    public RegisterRelativePointer(int offset, X64PreservedRegister register) {
        this.offset = offset;
        this.register = register;
    }

    @Override
    public Instruction.Size getSuffix() {
        return Instruction.Size.QUAD;
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        register.markUsed(i, usedRegs);
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        register.prioritizeRegisters(mapping);
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        register.markDefined(i, usedRegs);
    }

    @Override
    public String toString() {
        return offset + "(" + register.toString() + ")";
    }
}

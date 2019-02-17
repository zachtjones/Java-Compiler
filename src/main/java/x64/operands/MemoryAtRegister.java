package x64.operands;

import x64.X64InstructionSize;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

public class MemoryAtRegister implements Operand {
    private final X64PreservedRegister source;

    public MemoryAtRegister(X64PreservedRegister source) {
        this.source = source;
    }

    @Override
    public X64InstructionSize getSuffix() {
        return X64InstructionSize.QUAD;
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        source.markDefined(i, usedRegs);
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        source.markUsed(i, usedRegs);
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        source.prioritizeRegisters(mapping);
    }

    @Override
    public String toString() {
        return "(" + source + ")";
    }
}

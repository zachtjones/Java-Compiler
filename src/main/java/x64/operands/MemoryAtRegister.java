package x64.operands;

import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

public class MemoryAtRegister implements SourceOperand, DestinationOperand {
    private final X64RegisterOperand source;

    public MemoryAtRegister(X64RegisterOperand source) {
        this.source = source;
    }

    @Override
    public Instruction.Size getSuffix() {
        return Instruction.Size.QUAD;
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
    public void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        source.swapOut(mapping);
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

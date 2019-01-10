package x64.operands;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.allocation.X64NativeRegister;

import java.util.Map;

/** Represents a memory displacement from a register with a known constant integer offset */
public class RegisterRelativePointer implements SourceOperand, DestinationOperand {

    private final int offset;
    private X64RegisterOperand javaEnvPointer;

    public RegisterRelativePointer(int offset, X64RegisterOperand javaEnvPointer) {
        this.offset = offset;
        this.javaEnvPointer = javaEnvPointer;
    }

    @Override
    public Instruction.Size getSuffix() {
        return Instruction.Size.QUAD;
    }

    @Override
    public String toString() {
        return offset + "(" + javaEnvPointer.toString() + ")";
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        javaEnvPointer.markUsed(i, usedRegs);
    }

    @Override
    public void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        javaEnvPointer.swapOut(mapping);
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        javaEnvPointer.markDefined(i, usedRegs);
    }
}

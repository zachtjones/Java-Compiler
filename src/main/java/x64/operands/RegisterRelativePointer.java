package x64.operands;

import x64.Instruction;
import x64.allocation.RegistersUsed;

/** Represents a memory displacement from a register with a known constant integer offset */
public class RegisterRelativePointer implements SourceOperand, DestinationOperand {

    private final int offset;
    private final X64PreservedRegister javaEnvPointer;

    public RegisterRelativePointer(int offset, X64PreservedRegister javaEnvPointer) {
        this.offset = offset;
        this.javaEnvPointer = javaEnvPointer;
    }

    @Override
    public Instruction.Size getSuffix() {
        return Instruction.Size.QUAD;
    }

    @Override
    public String assemblyRep() {
        return offset + "(" + javaEnvPointer.assemblyRep() + ")";
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(javaEnvPointer, i);
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(javaEnvPointer, i);
    }
}

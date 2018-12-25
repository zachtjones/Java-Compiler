package x64.operands;

import intermediate.Register;
import x64.Instruction;

/** This is an abstraction of a hardware register that is preserved across function calls */
public class X64PreservedRegister implements SourceOperand, DestinationOperand {
    
    private final int number;
    private final Instruction.Size size;
    public X64PreservedRegister(int number, Instruction.Size size) {
        this.number = number;
        this.size = size;
    }

    /** Helper method to convert from an intermediate register to a x64 one. */
    public static X64PreservedRegister fromILRegister(Register intermediate) {
        return new X64PreservedRegister(intermediate.num, intermediate.x64Type());
    }

    public static X64PreservedRegister newTempQuad(int number) {
        return new X64PreservedRegister(number, Instruction.Size.QUAD);
    }

    @Override
    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public String assemblyRep() {
        return "%" + size.size + number;
    }
}

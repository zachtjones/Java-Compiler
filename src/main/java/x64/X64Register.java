package x64;

import intermediate.Register;

/** This is an abstraction of a hardware register, broken into 3 types */
public class X64Register implements SourceOperand, DestinationOperand {

    public final static String TEMPORARY = "t";
    public final static String ARGUMENT = "a";
    public final static X64Register RETURN = new X64Register(0, "r", Instruction.Size.QUAD);

    private final int number;
    private final String type;
    private final Instruction.Size size;
    public X64Register(int number, String type, Instruction.Size size) {
        this.number = number;
        this.type = type;
        this.size = size;
    }

    /** Helper method to convert from an intermediate register to a x64 one. */
    public static X64Register fromILRegister(Register intermediate) {
        return new X64Register(intermediate.num, X64Register.TEMPORARY, intermediate.x64Type());
    }

    public static X64Register newTempQuad(int number) {
        return new X64Register(number, X64Register.TEMPORARY, Instruction.Size.QUAD);
    }

    @Override
    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public String assemblyRep() {
        return "%" + size.size + number + type;
    }
}

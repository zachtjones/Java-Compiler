package x64;

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

    @Override
    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public String assemblyRep() {
        return "%" + size.size + number + type;
    }
}

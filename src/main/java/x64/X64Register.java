package x64;

/** This is an abstraction of a hardware register, broken into 3 types */
public class X64Register {

    public final static String TEMPORARY = "t";
    public final static String ARGUMENT = "a";
    public final static X64Register RETURN = new X64Register(0, "r");

    private int number;
    private String type;
    public X64Register(int number, String type) {
        this.number = number;
        this.type = type;
    }

    @Override
    public String toString() {
        return "%" + type + number;
    }
}

package x64;

public class X64Context {

    private int tempCounter;
    public final String javaName;

    public X64Context(String javaName) {
        this.javaName = javaName;
    }

    public void clearCounter() {
        tempCounter = -1;
    }

    public X64Register getTemporaryRegister(Instruction.Size size) {
        tempCounter++;
        return new X64Register(tempCounter, X64Register.TEMPORARY, size);
    }

    public X64Register getArgumentRegister(int number, Instruction.Size size) {
        return new X64Register(number, X64Register.ARGUMENT, size);
    }

    public X64Register getReturnRegister() {
        return X64Register.RETURN;
    }
}

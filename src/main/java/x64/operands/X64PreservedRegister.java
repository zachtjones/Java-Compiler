package x64.operands;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;

/** This is an abstraction of a hardware register that is preserved across function calls */
public class X64PreservedRegister {

    private final int number;
    private final X64InstructionSize size;
    public X64PreservedRegister(int number, @NotNull X64InstructionSize size) {
        this.number = number;
        this.size = size;
    }

    public X64InstructionSize getSuffix() {
        return size;
    }

    @Override
    public int hashCode() {
        return number ^ size.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof X64PreservedRegister &&
            ("%" + ((X64PreservedRegister) other).size.size + ((X64PreservedRegister) other).number).equals("%" + size.size + number);
    }

    @Override
    public String toString() {
        return "%" + size.size + number;
    }
}

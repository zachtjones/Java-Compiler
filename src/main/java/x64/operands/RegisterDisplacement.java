package x64.operands;

import org.jetbrains.annotations.NotNull;

/** Represents a memory displacement from a register with a known constant integer offset */
public class RegisterDisplacement {

    public final int offset;

    @NotNull public final X64NativeRegister register;

    public RegisterDisplacement(int offset, @NotNull X64NativeRegister register) {
        this.offset = offset;
        this.register = register;
    }

    @Override
    public String toString() {
        return offset + "(" + register.toString() + ")";
    }
}

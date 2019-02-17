package x64.operands;

import org.jetbrains.annotations.NotNull;

/** Represents a memory displacement from a register with a known constant integer offset */
public class PseudoRegDisplacement {

    private final int offset;

    @NotNull private final X64PreservedRegister register;

    public PseudoRegDisplacement(int offset, @NotNull X64PreservedRegister register) {
        this.offset = offset;
        this.register = register;
    }

    /** Returns the register that this is an offset of */
    @NotNull
    public X64PreservedRegister getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return offset + "(" + register.toString() + ")";
    }
}

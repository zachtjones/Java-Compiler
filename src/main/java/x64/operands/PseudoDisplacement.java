package x64.operands;

import org.jetbrains.annotations.NotNull;

/** Represents a memory displacement from a register with a known constant integer offset */
public class PseudoDisplacement {

    public final int offset;

    @NotNull public final X64PseudoRegister register;

    public PseudoDisplacement(int offset, @NotNull X64PseudoRegister register) {
        this.offset = offset;
        this.register = register;
    }

    @Override
    public String toString() {
        return offset + "(" + register.toString() + ")";
    }
}

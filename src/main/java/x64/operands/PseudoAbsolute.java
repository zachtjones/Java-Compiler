package x64.operands;

import org.jetbrains.annotations.NotNull;

public class PseudoAbsolute {
    @NotNull
    public final X64PseudoRegister source;

    public PseudoAbsolute(@NotNull X64PseudoRegister source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "(" + source + ")";
    }
}

package x64.operands;

import org.jetbrains.annotations.NotNull;

public class RegAbsolute {
    @NotNull
    public final X64NativeRegister source;

    public RegAbsolute(@NotNull X64NativeRegister source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "(" + source + ")";
    }
}

package x64.operands;

import org.jetbrains.annotations.NotNull;

public class MemoryAtPseudo {
    @NotNull private final X64PreservedRegister source;

    public MemoryAtPseudo(@NotNull X64PreservedRegister source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "(" + source + ")";
    }
}

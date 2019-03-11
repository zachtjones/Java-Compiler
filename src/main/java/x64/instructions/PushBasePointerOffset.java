package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;

/** represents pushing memory at a base pointer offset to the stack. */
public class PushBasePointerOffset extends Instruction {
    private BasePointerOffset basePointerOffset;

    public PushBasePointerOffset(@NotNull BasePointerOffset basePointerOffset) {
        this.basePointerOffset = basePointerOffset;
    }

    @Override
    public String assemblyRepresentation() {
        return "push\t" + basePointerOffset;
    }
}

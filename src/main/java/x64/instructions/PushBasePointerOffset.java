package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;

/** represents pushing memory at a base pointer offset to the stack. */
public class PushBasePointerOffset extends Instruction {
    private BPOffset basePointerOffset;

    public PushBasePointerOffset(@NotNull BPOffset basePointerOffset) {
        this.basePointerOffset = basePointerOffset;
    }

    @Override
    public String assemblyRepresentation() {
        return "push\t" + basePointerOffset;
    }
}

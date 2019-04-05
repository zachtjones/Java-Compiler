package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;

/** represents pushing memory at a base pointer offset to the stack. */
public class PushBasePointerOffset extends Instruction {

	/** Pushes the memory at the base pointer offset onto the stack. */
    public PushBasePointerOffset(@NotNull BPOffset basePointerOffset) {
		super("\tpush " + basePointerOffset);
    }
}

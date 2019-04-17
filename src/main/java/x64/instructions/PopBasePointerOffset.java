package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;

/** represents popping memory at a base pointer offset to the stack. */
public class PopBasePointerOffset extends Instruction {

	/** pops the memory at the base pointer offset onto the stack. */
    public PopBasePointerOffset(@NotNull BPOffset basePointerOffset) {
		super("\tpop " + basePointerOffset);
    }
}

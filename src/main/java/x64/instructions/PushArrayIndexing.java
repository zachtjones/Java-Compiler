package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegIndexing;

public class PushArrayIndexing extends Instruction {

	/** Represents a push off the stack into the memory at the indexing offset. */
	public PushArrayIndexing(@NotNull RegIndexing indexing) {
		super("\tpushq " + indexing);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class PopArrayIndex extends Instruction {

	/** Represents a pop off the stack into the memory at the indexing offset. */
	public PopArrayIndex(@NotNull X64Register base, @NotNull X64Register index, int scaling) {
		super("popq (" + base + ", " + index + ", " + scaling + ")");
	}
}

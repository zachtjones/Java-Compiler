package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;
import x64.operands.Immediate;

public class MoveImmToBPOffset extends BinaryImmToBPOffset {


	public MoveImmToBPOffset(@NotNull Immediate source,
							 @NotNull BPOffset destination) {
		super("mov", source, destination);
	}
}

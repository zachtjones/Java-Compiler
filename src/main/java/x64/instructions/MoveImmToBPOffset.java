package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.Immediate;

public class MoveImmToBPOffset extends BinaryImmToBPOffset {


	public MoveImmToBPOffset(@NotNull Immediate source, @NotNull BPOffset destination,
							 @NotNull X64InstructionSize size) {
		super("mov", source, destination, size);
	}
}

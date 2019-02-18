package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.Immediate;

public class MoveImmToBasePointerOffsetInstruction extends BinaryImmToBasePointerOffsetInstruction {


	public MoveImmToBasePointerOffsetInstruction(@NotNull Immediate source,
												 @NotNull BasePointerOffset destination) {
		super("mov", source, destination);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;

public class MoveRegToBasePointerOffset extends BinaryRegToBasePointerOffsetInstruction {


	public MoveRegToBasePointerOffset(@NotNull X64NativeRegister source,
									  @NotNull BasePointerOffset destination) {
		super("mov", source, destination);
	}
}

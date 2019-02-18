package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

public class MoveBasePointerOffsetToReg extends BinaryBasePointerOffsetToRegInstruction {


	public MoveBasePointerOffsetToReg(@NotNull BasePointerOffset source,
									  @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

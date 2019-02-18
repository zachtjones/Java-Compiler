package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;

public class SubtractBasePointerOffsetToReg extends BinaryBasePointerOffsetToReg {
	public SubtractBasePointerOffsetToReg(@NotNull BasePointerOffset source, @NotNull X64NativeRegister destination) {
		super("sub", source, destination);
	}
}

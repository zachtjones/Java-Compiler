package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

public class SubtractRegToReg extends BinaryRegToReg {
	public SubtractRegToReg(@NotNull X64NativeRegister source, @NotNull X64NativeRegister destination) {
		super("sub", source, destination);
	}
}

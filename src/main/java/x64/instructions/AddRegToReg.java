package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

public class AddRegToReg extends BinaryRegToReg {
	public AddRegToReg(@NotNull X64NativeRegister source, @NotNull X64NativeRegister destination) {
		super("add", source, destination);
	}
}

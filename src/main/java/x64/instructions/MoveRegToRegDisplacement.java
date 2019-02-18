package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64NativeRegister;

public class MoveRegToRegDisplacement extends BinaryRegToRegDisplacement {

	public MoveRegToRegDisplacement(@NotNull X64NativeRegister source, @NotNull RegDisplacement destination) {
		super("mov", source, destination);
	}
}

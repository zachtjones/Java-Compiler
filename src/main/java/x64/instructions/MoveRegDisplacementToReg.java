package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64NativeRegister;

public class MoveRegDisplacementToReg extends BinaryRegDisplacementToReg {

	public MoveRegDisplacementToReg(@NotNull RegDisplacement source, @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

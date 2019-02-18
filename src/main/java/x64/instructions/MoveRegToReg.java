package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

public class MoveRegToReg extends BinaryRegToReg {


	public MoveRegToReg(@NotNull X64NativeRegister source,
						@NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

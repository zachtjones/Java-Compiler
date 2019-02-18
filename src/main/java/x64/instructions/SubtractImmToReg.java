package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

/** Represents a sub $number, %register */
public class SubtractImmToReg extends BinaryImmToReg {

	public SubtractImmToReg(@NotNull Immediate source, @NotNull X64NativeRegister destination) {
		super("sub", source, destination);
	}
}

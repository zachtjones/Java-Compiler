package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

/** Represents a add $number, %register */
public class AddImmReg extends BinaryImmToReg {

	public AddImmReg(@NotNull Immediate source, @NotNull X64NativeRegister destination) {
		super("add", source, destination);
	}
}

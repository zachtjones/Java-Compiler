package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64Register;

/** Represents a add $number, %register */
public class AddImmReg extends BinaryImmToReg {

	public AddImmReg(@NotNull Immediate source, @NotNull X64Register destination) {
		super("add", source, destination);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64Register;

/** Represents a sub $number, %register */
public class SubtractImmToReg extends BinaryImmToReg {

	public SubtractImmToReg(@NotNull Immediate source, @NotNull X64Register destination) {
		super("sub", source, destination);
	}
}

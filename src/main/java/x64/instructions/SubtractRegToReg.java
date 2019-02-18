package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class SubtractRegToReg extends BinaryRegToReg {
	public SubtractRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		super("sub", source, destination);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class AddRegToReg extends BinaryRegToReg {
	public AddRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		super("add", source, destination);
	}
}

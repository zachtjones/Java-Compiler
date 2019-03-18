package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class AddRegToReg extends BinaryRegToReg {
	public AddRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
					   @NotNull X64InstructionSize size) {
		super("add", source, destination, size);
	}
}

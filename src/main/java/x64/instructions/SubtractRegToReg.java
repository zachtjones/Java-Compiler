package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class SubtractRegToReg extends BinaryRegToReg {
	public SubtractRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
							@NotNull X64InstructionSize size) {
		super("sub", source, destination, size);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

/** Represents a move from a register to a displacement of a register. */
public class MoveRegToRegDisplacement extends BinaryRegToRegDisplacement {

	public MoveRegToRegDisplacement(@NotNull X64Register source, @NotNull RegDisplacement destination,
									@NotNull X64InstructionSize size) {
		super("mov", source, destination, size);
	}
}

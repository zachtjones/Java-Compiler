package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegAbsolute;
import x64.operands.X64Register;

/** Represents the register move from register to a pointer. */
public class MoveRegToRegAbsolute extends BinaryRegToRegAbsolute {
	public MoveRegToRegAbsolute(@NotNull X64Register source, @NotNull RegAbsolute destination,
								@NotNull X64InstructionSize suffix) {

		super("mov", source, destination, suffix);
	}
}

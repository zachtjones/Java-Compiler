package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

/**
 * Represents a push of a native register to the stack, usually in the function prologue.
 */
public class PushReg extends Instruction {

	/** Pushes a register's value to the top of the stack. */
	public PushReg(@NotNull X64Register reg) {
		super("\tpushq " + reg);
	}
}

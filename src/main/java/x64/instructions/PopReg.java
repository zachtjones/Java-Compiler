package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class PopReg extends Instruction {

	/** Pops a register off the stack. This always uses 8 bytes. */
	public PopReg(@NotNull X64Register reg) {
		super("\tpopq " + reg);
	}
}

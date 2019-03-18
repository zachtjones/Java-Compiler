package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.Immediate;
import x64.operands.X64Register;

/** Represents an add $number, %register */
public class AddImmReg extends BinaryImmToReg {

	public AddImmReg(@NotNull Immediate source, @NotNull X64Register destination, @NotNull X64InstructionSize size) {
		super("add", source, destination, size);
	}
}

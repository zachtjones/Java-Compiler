package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.Immediate;
import x64.operands.X64Register;

public class MoveImmToReg extends BinaryImmToReg {


	public MoveImmToReg(@NotNull Immediate source, @NotNull X64Register destination,
						@NotNull X64InstructionSize size) {

		super("mov", source, destination, size);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

public class MoveRIPRelativeToReg extends BinaryRIPRelativeToReg {
	public MoveRIPRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64Register destination,
								@NotNull X64InstructionSize size) {
		super("mov", source, destination, size);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class AddBPOffsetToReg extends BinaryBPOffsetToReg {
	public AddBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination,
							@NotNull X64InstructionSize size) {

		super("add", source, destination, size);
	}
}

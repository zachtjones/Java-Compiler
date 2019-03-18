package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class SubtractBasePointerOffsetToReg extends BinaryBasePointerOffsetToReg {
	public SubtractBasePointerOffsetToReg(@NotNull BasePointerOffset source, @NotNull X64Register destination,
										  @NotNull X64InstructionSize size) {
		super("sub", source, destination, size);
	}
}

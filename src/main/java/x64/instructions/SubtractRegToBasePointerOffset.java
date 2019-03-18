package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class SubtractRegToBasePointerOffset extends BinaryRegToBasePointerOffset {
	public SubtractRegToBasePointerOffset(@NotNull X64Register source, @NotNull BasePointerOffset destination,
										  @NotNull X64InstructionSize size) {
		super("sub", source, destination, size);
	}
}

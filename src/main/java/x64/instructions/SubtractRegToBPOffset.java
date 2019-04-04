package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class SubtractRegToBPOffset extends BinaryRegToBPOffset {
	public SubtractRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination,
								 @NotNull X64InstructionSize size) {
		super("sub", source, destination, size);
	}
}

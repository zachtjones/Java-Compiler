package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class MoveBPOffsetToReg extends BinaryBPOffsetToReg {


	public MoveBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination,
							 @NotNull X64InstructionSize size) {
		super("mov", source, destination, size);
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class ZeroExtendBPOffsetToReg extends BinaryBPOffsetToReg {

	public ZeroExtendBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination,
								   @NotNull X64InstructionSize sourceSize,
								   @NotNull X64InstructionSize destinationSize) {

		super("movz", source, destination, sourceSize, destinationSize);
	}
}

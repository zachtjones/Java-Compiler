package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToRIPRelative;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovePseudoToRIPRelative extends BinaryPseudoToRIPRelative {

	public MovePseudoToRIPRelative(@NotNull X64PseudoRegister source,
								   @NotNull RIPRelativeData destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(source)) {
			return Collections.singletonList(
				new MoveRegToRIPRelative(
					context.getRegister(source),
					destination,
					source.getSuffix()
				)
			);
		} else {
			// need temp, can't have 2 memory operands in 1 instruction
			return Arrays.asList(
				new MoveBasePointerOffsetToReg(
					context.getBasePointer(source),
					context.getScratchRegister(),
					source.getSuffix()
				),
				new MoveRegToRIPRelative(
					context.getScratchRegister(),
					destination,
					source.getSuffix()
				)
			);
		}
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BasePointerOffset;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

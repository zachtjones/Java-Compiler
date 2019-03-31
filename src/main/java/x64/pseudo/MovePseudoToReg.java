package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToReg;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Collections;
import java.util.List;

public class MovePseudoToReg extends BinaryPseudoToReg {

	public MovePseudoToReg(@NotNull X64PseudoRegister source,
						   @NotNull X64Register destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		if (context.isRegister(source)) {
			return Collections.singletonList(
				new MoveRegToReg(
					context.getRegister(source),
					destination,
					source.getSuffix()
				)
			);
		} else {
			return Collections.singletonList(
				new MoveBasePointerOffsetToReg(
					context.getBasePointer(source),
					destination,
					source.getSuffix()
				)
			);
		}
	}
}

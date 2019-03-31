package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.MoveRegToReg;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovePseudoToPseudo extends BinaryPseudoToPseudo {

	public MovePseudoToPseudo(@NotNull X64PseudoRegister source,
							  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		if (context.isRegister(source)) {
			if (context.isRegister(destination)) {
				// both are real registers
				return Collections.singletonList(
					new MoveRegToReg(
						context.getRegister(source),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// destination is a local
				return Collections.singletonList(
					new MoveRegToBasePointerOffset(
						context.getRegister(source),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (context.isRegister(destination)) {
				// source -- base pointer, destination, real
				return Collections.singletonList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// both are base pointer offset, need to use the temporary one
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

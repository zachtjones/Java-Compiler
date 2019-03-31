package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegAbsoluteToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.PseudoAbsolute;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovePseudoAbsoluteToPseudo extends BinaryPseudoAbsoluteToPseudo {

	public MovePseudoAbsoluteToPseudo(@NotNull PseudoAbsolute source,
									  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(source.source)) {
			if (context.isRegister(destination)) {
				return Collections.singletonList(
					new MoveRegAbsoluteToReg(
						context.getRegister(source.source),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				return Arrays.asList(
					new MoveRegAbsoluteToReg(
						context.getRegister(source.source),
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
		} else {
			if (context.isRegister(destination)) {
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source.source),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegAbsoluteToReg(
						context.getScratchRegister(),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// both are mapped to memory, but the first is already mapped to memory
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source.source),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegAbsoluteToReg(
						context.getScratchRegister(),
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

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveBPOffsetToReg;
import x64.instructions.MoveRegDisplacementToReg;
import x64.instructions.MoveRegToBPOffset;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovePseudoDisplacementToPseudo extends BinaryPseudoDisplacementToPseudo {
	public MovePseudoDisplacementToPseudo(@NotNull PseudoDisplacement source,
										  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		// example: mov 8(%q1), %q2
		if (context.isRegister(source.register)) {
			if (context.isRegister(destination)) {
				// mov 8(%r1), %r2
				return Collections.singletonList(
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getRegister(source.register)),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// destination is mapped to base pointer offset
				// mov 8(%r1), -16(%rbp) -- use temporary in the middle
				return Arrays.asList(
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getRegister(source.register)),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			// example: mov 8(%q1), %q2

			if (context.isRegister(destination)) {
				// mov 8(-16(%rbp)), %r2
				//  goes to:
				// mov -16(%rbp), %temp
				// mov 8(%temp), %r2
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.register),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getScratchRegister()),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);

			} else {
				// mov 8(-16(%rbp)), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// mov 8(%temp), %temp
				// mov %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.register),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getScratchRegister()),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

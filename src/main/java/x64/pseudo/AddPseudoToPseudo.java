package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.*;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddPseudoToPseudo extends BinaryPseudoToPseudo {
	public AddPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		// example: add %q1, %q2
		if (context.isRegister(source)) {
			if (context.isRegister(destination)) {
				// add %r1, %r2
				return Collections.singletonList(
					new AddRegToReg(
						context.getRegister(source),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// add %r1, -16(%rbp)
				return Collections.singletonList(
					new AddRegToBasePointerOffset(
						context.getRegister(source),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (context.isRegister(destination)) {
				// add -16(%rbp), %r2
				return Collections.singletonList(
					new AddBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getRegister(destination),
						source.getSuffix())
				);
			} else {
				// add -16(%rbp), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// add %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix()
					),
					new AddRegToBasePointerOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

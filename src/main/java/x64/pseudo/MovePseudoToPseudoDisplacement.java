package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoToPseudoDisplacement extends BinaryPseudoToPseudoDisplacement {

	public MovePseudoToPseudoDisplacement(@NotNull X64PseudoRegister source,
										  @NotNull PseudoDisplacement destination) {

		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		// destination is always a memory operand, the displacement

		// an example is useful to think of for this one:
		// mov %q1, 8(%q2)
		if (context.isRegister(source)) {
			if (context.isRegister(destination.register)) {
				// %q1 = %r12, %q2 = %r13
				// becomes: mov %r12, 8(%r13)
				return Collections.singletonList(
					new MoveRegToRegDisplacement(
						context.getRegister(source),
						new RegDisplacement(destination.offset, context.getRegister(destination.register))
					)
				);
			} else {
				// %q1 = %r12, %q2 = -16(%rbp)
				// would be: mov %r12, 8(16(%rbp)), which isn't possible
				// instead:
				//   mov 16(%rbp), %temp
				//   mov %r12, 8(%temp)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(destination.register),
						context.getScratchRegister(),
						source.getSuffix()
					),
					new MoveRegToRegDisplacement(
						context.getScratchRegister(),
						new RegDisplacement(destination.offset, context.getScratchRegister())
					)
				);
			}
		} else { // mov %q1, 8(%q2)
			if (context.isRegister(destination.register)) {
				// %q1 = -16(%rbp), %q2 = %r14
				// becomes:
				// mov -16(%rbp), 8(%r14), which can't be done
				// mov -16(%rbp), %temp; mov %temp, 8(%r14)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix()
					),
					new MoveRegToRegDisplacement(
						context.getScratchRegister(),
						new RegDisplacement(destination.offset, context.getRegister(destination.register))
					)
				);
			} else {
				// %q1 = -24(%rbp), %q2 = -16(%rbp)
				//  mov -24(%rbp), 8(-16(%rbp))

				// want to do these 3 instructions (but uses 2 temps, which we don't have)
				//  mov -24(%rbp), %temp1  # source loaded
				//  mov -16(%rbp), %temp2  # destination loaded
				//  mov %temp1, 8(%temp2)

				// instead can push/pop from stack: (may complicate optimizations)
				//   push -24(%rbp)
				//   mov  -16(%rbp), %temp
				//   pop  8(%temp)

				return Arrays.asList(
					new PushBasePointerOffset(
						context.getBasePointer(source)
					),
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(destination.register),
						context.getScratchRegister(),
						source.getSuffix()
					),
					new PopDisplacement(
						new RegDisplacement(destination.offset, context.getScratchRegister())
					)
				);
			}
		}
	}
}

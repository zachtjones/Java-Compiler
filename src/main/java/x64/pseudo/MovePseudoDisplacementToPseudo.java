package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegDisplacementToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoDisplacementToPseudo extends BinaryPseudoDisplacementToPseudo {
	public MovePseudoDisplacementToPseudo(@NotNull PseudoDisplacement source,
										  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		// example: mov 8(%q1), %q2
		if (mapping.containsKey(source.register)) {
			if (mapping.containsKey(destination)) {
				// mov 8(%r1), %r2
				return Collections.singletonList(
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, mapping.get(source.register)),
						mapping.get(destination),
						destination.getSuffix()
					)
				);
			} else {
				// destination is mapped to base pointer offset
				// mov 8(%r1), -16(%rbp) -- use temporary in the middle
				return Arrays.asList(
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, mapping.get(source.register)),
						temporaryImmediate,
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						temporaryImmediate,
						locals.get(destination)
					)
				);
			}
		} else {
			// example: mov 8(%q1), %q2

			if (mapping.containsKey(destination)) {
				// mov 8(-16(%rbp)), %r2
				//  goes to:
				// mov -16(%rbp), %temp
				// mov 8(%temp), %r2
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						locals.get(source.register),
						temporaryImmediate,
						destination.getSuffix()
					),
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, temporaryImmediate),
						mapping.get(destination),
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
					new MoveBasePointerOffsetToReg(
						locals.get(source.register),
						temporaryImmediate,
						destination.getSuffix()
					),
					new MoveRegDisplacementToReg(
						new RegDisplacement(source.offset, temporaryImmediate),
						temporaryImmediate,
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(temporaryImmediate, locals.get(destination))
				);
			}
		}
	}
}

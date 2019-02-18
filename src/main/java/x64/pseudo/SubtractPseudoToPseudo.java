package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubtractPseudoToPseudo extends BinaryPseudoToPseudo {
	public SubtractPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		// example: sub %q1, %q2
		if (mapping.containsKey(source)) {
			if (mapping.containsKey(destination)) {
				// sub %r1, %r2
				return Collections.singletonList(
					new SubtractRegToReg(mapping.get(source), mapping.get(destination))
				);
			} else {
				// sub %r1, -16(%rbp)
				return Collections.singletonList(
					new SubtractRegToBasePointerOffset(mapping.get(source), locals.get(destination))
				);
			}
		} else {
			if (mapping.containsKey(destination)) {
				// sub -16(%rbp), %r2
				return Collections.singletonList(
					new SubtractBasePointerOffsetToReg(locals.get(source), mapping.get(destination))
				);
			} else {
				// sub -16(%rbp), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// sub %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(locals.get(source), temporaryImmediate),
					new SubtractRegToBasePointerOffset(temporaryImmediate, locals.get(destination))
				);
			}
		}
	}
}

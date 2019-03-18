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

public class AddPseudoToPseudo extends BinaryPseudoToPseudo {
	public AddPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		// example: add %q1, %q2
		if (mapping.containsKey(source)) {
			if (mapping.containsKey(destination)) {
				// add %r1, %r2
				return Collections.singletonList(
					new AddRegToReg(
						mapping.get(source),
						mapping.get(destination),
						destination.getSuffix()
					)
				);
			} else {
				// add %r1, -16(%rbp)
				return Collections.singletonList(
					new AddRegToBasePointerOffset(
						mapping.get(source),
						locals.get(destination)
					)
				);
			}
		} else {
			if (mapping.containsKey(destination)) {
				// add -16(%rbp), %r2
				return Collections.singletonList(
					new AddBasePointerOffsetToReg(
						locals.get(source),
						mapping.get(destination),
						source.getSuffix())
				);
			} else {
				// add -16(%rbp), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// add %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						locals.get(source),
						temporaryImmediate,
						source.getSuffix()
					),
					new AddRegToBasePointerOffset(
						temporaryImmediate,
						locals.get(destination)
					)
				);
			}
		}
	}
}

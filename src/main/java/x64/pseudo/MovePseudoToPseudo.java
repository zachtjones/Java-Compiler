package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.MoveRegToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoToPseudo extends BinaryPseudoToPseudo {

	public MovePseudoToPseudo(@NotNull X64PseudoRegister source,
							  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {

		if (mapping.containsKey(source)) {
			if (mapping.containsKey(destination)) {
				// both are real registers
				return Collections.singletonList(
					new MoveRegToReg(
						mapping.get(source),
						mapping.get(destination),
						destination.getSuffix()
					)
				);
			} else {
				// destination is a local
				return Collections.singletonList(
					new MoveRegToBasePointerOffset(
						mapping.get(source),
						locals.get(destination)
					)
				);
			}
		} else {
			if (mapping.containsKey(destination)) {
				// source -- base pointer, destination, real
				return Collections.singletonList(
					new MoveBasePointerOffsetToReg(
						locals.get(source),
						mapping.get(destination),
						destination.getSuffix()
					)
				);
			} else {
				// both are base pointer offset, need to use the temporary one
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						locals.get(source),
						temporaryImmediate,
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						temporaryImmediate,
						locals.get(destination)
					)
				);
			}
		}
	}
}

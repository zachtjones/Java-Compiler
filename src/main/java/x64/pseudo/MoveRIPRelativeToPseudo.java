package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveRIPRelativeToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoveRIPRelativeToPseudo extends BinaryRIPRelativeToPseudo {

	public MoveRIPRelativeToPseudo(@NotNull RIPRelativeData source,
								   @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		if (mapping.containsKey(destination)) {
			return Collections.singletonList(
				new MoveRIPRelativeToReg(
					source,
					mapping.get(destination),
					destination.getSuffix()
				)
			);
		} else {
			// need temp, can't have 2 memory operands in 1 instruction
			return Arrays.asList(
				new MoveRIPRelativeToReg(
					source,
					temporaryImmediate,
					destination.getSuffix()
				),
				new MoveRegToBasePointerOffset(
					temporaryImmediate,
					locals.get(destination),
					destination.getSuffix()
				)
			);
		}
	}
}

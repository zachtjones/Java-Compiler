package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoToReg extends BinaryPseudoToReg {

	public MovePseudoToReg(@NotNull X64PseudoRegister source,
						   @NotNull X64Register destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {

		if (mapping.containsKey(source)) {
			return Collections.singletonList(
				new MoveRegToReg(
					mapping.get(source),
					destination,
					source.getSuffix()
				)
			);
		} else {
			return Collections.singletonList(
				new MoveBasePointerOffsetToReg(
					locals.get(source),
					destination,
					source.getSuffix()
				)
			);
		}
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveImmToBasePointerOffset;
import x64.instructions.MoveImmToReg;
import x64.operands.BasePointerOffset;
import x64.operands.Immediate;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoveImmToPseudo extends BinaryImmediateToPseudo {

	public MoveImmToPseudo(@NotNull Immediate source, @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		if (mapping.containsKey(destination)) {
			return Collections.singletonList(
				new MoveImmToReg(source, mapping.get(destination))
			);
		} else {
			return Collections.singletonList(
				new MoveImmToBasePointerOffset(source, locals.get(destination))
			);
		}
	}
}

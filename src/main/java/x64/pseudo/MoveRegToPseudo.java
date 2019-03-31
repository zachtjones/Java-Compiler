package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.MoveRegToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoveRegToPseudo extends BinaryRegToPseudoReg {

	public MoveRegToPseudo(@NotNull X64Register source,
						   @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(destination)) {
			return Collections.singletonList(
				new MoveRegToReg(
					source,
					context.getRegister(destination),
					destination.getSuffix()
				)
			);
		} else {
			return Collections.singletonList(
				new MoveRegToBasePointerOffset(
					source,
					context.getBasePointer(destination),
					destination.getSuffix()
				)
			);
		}
	}
}

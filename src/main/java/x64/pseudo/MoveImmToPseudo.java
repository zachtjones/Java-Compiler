package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveImmToBasePointerOffset;
import x64.instructions.MoveImmToReg;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;

public class MoveImmToPseudo extends BinaryImmediateToPseudo {

	public MoveImmToPseudo(@NotNull Immediate source, @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(destination)) {
			return Collections.singletonList(
				new MoveImmToReg(
					source,
					context.getRegister(destination),
					destination.getSuffix()
				)
			);
		} else {
			return Collections.singletonList(
				new MoveImmToBasePointerOffset(
					source,
					context.getBasePointer(destination)
				)
			);
		}
	}
}

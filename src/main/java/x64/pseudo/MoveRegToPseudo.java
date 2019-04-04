package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveRegToBPOffset;
import x64.instructions.MoveRegToReg;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Collections;
import java.util.List;

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
				new MoveRegToBPOffset(
					source,
					context.getBasePointer(destination),
					destination.getSuffix()
				)
			);
		}
	}
}

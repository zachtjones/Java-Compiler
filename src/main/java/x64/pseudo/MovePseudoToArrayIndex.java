package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.instructions.*;
import x64.operands.PseudoIndexing;
import x64.operands.RegIndexing;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

/**
 * Represents mov source, (base, index, scaling).
 */
public class MovePseudoToArrayIndex extends BinaryPseudoToArrayIndex {

	public MovePseudoToArrayIndex(@NotNull X64PseudoRegister source, @NotNull PseudoIndexing destination) {
		super("mov", source, destination);
	}

	@Override
	BinaryRegToArrayIndex createThisRegToArrayIndex(@NotNull X64Register source, @NotNull RegIndexing destination) {
		return new MoveRegToArrayIndex(source, destination, this.source.getSuffix());
	}

	@Override
	@NotNull List<@NotNull Instruction> allocateNoRegs(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// move -8(%rbp), offset(-16(%rbp), -24(%rbp), scaling)

		// move -16(%rbp), scratch1
		// move -24(%rbp), scratch2
		// push -8(%rbp)
		// pop (scratch1, scratch2, scaling)

		return Arrays.asList(
			new MoveBPOffsetToReg(
				context.getBasePointer(dest.base),
				context.getScratchRegister(),
				QUAD
			),
			new MoveBPOffsetToReg(
				context.getBasePointer(dest.index),
				context.getSecondScratch(),
				QUAD
			),
			new PushBasePointerOffset(
				context.getBasePointer(source)
			),
			new PopArrayIndex(
				context.getScratchRegister(),
				context.getSecondScratch(),
				dest.scaling
			)
		);
	}
}

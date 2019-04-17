package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.instructions.*;
import x64.operands.*;

import java.util.Arrays;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

public class MoveArrayIndexToPseudo extends BinaryArrayIndexToPseudo {

	public MoveArrayIndexToPseudo(@NotNull PseudoIndexing source, @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	BinaryArrayIndexToReg createThisArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination) {
		return new MoveArrayIndexToReg(source, destination, this.destination.getSuffix());
	}

	@Override
	BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
		return new MoveRegToBPOffset(source, destination, this.destination.getSuffix());
	}

	@Override
	@NotNull List<@NotNull Instruction> allocateNoRegs(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// have to use stack or 3 temporary scratch registers, but we can't always allocate those

		// move offset(-16(%rbp), -24(%rbp), scaling), -8(%rbp)

		// To instructions we can encode:
		// move -16(%rbp), scratch1
		// move -24(%rbp), scratch2
		// lea offset(scratch1, scratch2, scaling), scratch1
		// move (scratch1), scratch2
		// move scratch2, -8(%rbp)

		return Arrays.asList(
			new MoveBPOffsetToReg(
				context.getBasePointer(source.base),
				context.getScratchRegister(),
				QUAD
			),
			new MoveBPOffsetToReg(
				context.getBasePointer(source.index),
				context.getSecondScratch(),
				QUAD
			),
			new LoadEffectiveAddressIndexingToReg(
				source.allocate(context.getScratchRegister(), context.getSecondScratch()),
				context.getScratchRegister()
			),
			new MoveRegAbsoluteToReg(
				new RegAbsolute(context.getScratchRegister()),
				context.getSecondScratch(),
				destination.getSuffix()
			),
			new MoveRegToBPOffset(
				context.getSecondScratch(),
				context.getBasePointer(destination),
				destination.getSuffix()
			)
		);
	}
}

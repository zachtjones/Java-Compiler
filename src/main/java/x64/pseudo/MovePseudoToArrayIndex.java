package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.instructions.*;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

/**
 * Represents mov source, (base, index, scaling).
 */
public class MovePseudoToArrayIndex implements PseudoInstruction {
	@NotNull private final X64PseudoRegister source, base, index;
	private final int scaling;

	public MovePseudoToArrayIndex(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister base,
								  @NotNull X64PseudoRegister index, int scaling) {
		this.source = source;
		this.base = base;
		this.index = index;
		this.scaling = scaling;
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) throws NotSecondScratchException {

		// mov source, (base, index, scaling).
		if (context.isRegister(source)) {
			if (context.isRegister(base)) {
				if (context.isRegister(index)) {
					// all 3 are hardware registers, just a simple move
					return Collections.singletonList(
						new MoveRegToArrayIndex(context.getRegister(source), context.getRegister(base), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source, base are regs, index is a base pointer offset
					// move the index to temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(context.getBasePointer(index), context.getScratchRegister(), QUAD),
						new MoveRegToArrayIndex(context.getRegister(source), context.getRegister(base), context.getScratchRegister(),
							scaling, source.getSuffix()
						)
					);
				}
			} else {
				if (context.isRegister(index)) {
					// index and source are registers; base is not
					// move the base to the temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(context.getBasePointer(base), context.getScratchRegister(), QUAD),
						new MoveRegToArrayIndex(context.getRegister(source), context.getScratchRegister(), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source is a register, index and base are not
					// simply move the base pointer offsets into index and base
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(base),
							context.getScratchRegister(),
							QUAD
						),
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(index),
							context.getSecondScratch(),
							QUAD
						),
						new MoveRegToArrayIndex(
							context.getRegister(source),
							context.getScratchRegister(),
							context.getSecondScratch(),
							scaling,
							source.getSuffix()
						)
					);
				}
			}
		} else {
			if (context.isRegister(base)) {
				if (context.isRegister(index)) {
					// base and index are registers, source is not
					// move source, tempImm; do the indexing operation with tempImm as the source
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(context.getBasePointer(source), context.getScratchRegister(), source.getSuffix()),
						new MoveRegToArrayIndex(context.getScratchRegister(), context.getRegister(base), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// base is register, source and index are not.
					// move -8(rbp), (%base, -16(%rbp), scaling)

					// can move the source to scratch1
					// move the index to scratch2
					// move scratch1, (%base, %scratch2, scaling)
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(source),
							context.getScratchRegister(),
							source.getSuffix()
						),
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(index),
							context.getSecondScratch(),
							QUAD
						),
						new MoveRegToArrayIndex(
							context.getScratchRegister(),
							context.getRegister(base),
							context.getSecondScratch(),
							scaling,
							source.getSuffix()
						)
					);
				}
			} else {
				if (context.isRegister(index)) {
					// index is register, source and base are not
					// move -8(rbp), (-16(%rbp), %index, scaling)

					// can move the source to scratch1
					// move the base to scratch2
					// move scratch1, (scratch2, %index, scaling)
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(source),
							context.getScratchRegister(),
							source.getSuffix()
						),
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(base),
							context.getSecondScratch(),
							QUAD
						),
						new MoveRegToArrayIndex(
							context.getScratchRegister(),
							context.getSecondScratch(),
							context.getRegister(index),
							scaling,
							source.getSuffix()
						)
					);
				} else {
					// none of them are registers, they are all allocated to base pointer offsets.
					// move -8(%rbp), (-16(%rbp), -24(%rbp), scaling)

					// move -16(%rbp), scratch1
					// move -24(%rbp), scratch2
					// push -8(%rbp)
					// pop (scratch1, scratch2, scaling)

					return Arrays.asList(
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(base),
							context.getScratchRegister(),
							QUAD
						),
						new MoveBasePointerOffsetToReg(
							context.getBasePointer(index),
							context.getSecondScratch(),
							QUAD
						),
						new PushBasePointerOffset(
							context.getBasePointer(source)
						),
						new PopArrayIndex(
							context.getScratchRegister(),
							context.getSecondScratch(),
							scaling
						)
					);
				}
			}
		}

		// also the subclasses of binary statements should just have a method that creates themselves, with
		//  binary instruction doing the allocation.
		// This way the allocation problem is only unique for each type of instruction, not based on the opcode.
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.PseudoIndexing;
import x64.operands.RegIndexing;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

public abstract class BinaryPseudoToArrayIndex implements PseudoInstruction {
	@NotNull protected final X64PseudoRegister source;
	@NotNull final PseudoIndexing dest;
	@NotNull final String opcode;

	BinaryPseudoToArrayIndex(@NotNull String opcode, @NotNull X64PseudoRegister source,
							 @NotNull PseudoIndexing destination) {
		this.source = source;
		this.dest = destination;
		this.opcode = opcode;
	}

	@Override
	public final void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source, i);
		// also uses the registers that are part of the indexing
		usedRegs.markUsed(dest.base, i);
		usedRegs.markUsed(dest.index, i);
	}

	/**
	 * Creates the subclass's binary op source, offset(base, index, scaling)
	 * @param source The register allocated to the source.
	 * @param destination The register indexing destination.
	 * @return The subclass.
	 */
	abstract BinaryRegToArrayIndex
		createThisRegToArrayIndex(@NotNull X64Register source, @NotNull RegIndexing destination);


	abstract @NotNull List<@NotNull Instruction> allocateNoRegs(@NotNull AllocationContext context)
		throws NotSecondScratchException;


	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// mov source, (base, index, scaling).
		if (context.isRegister(source)) {
			if (context.isRegister(dest.base)) {
				if (context.isRegister(dest.index)) {
					// all 3 are hardware registers, just a simple move
					return Collections.singletonList(
						createThisRegToArrayIndex(
							context.getRegister(source),
							dest.allocate(context.getRegister(dest.base), context.getRegister(dest.index))
						)
					);
				} else {
					// source, base are regs, index is a base pointer offset
					// move the index to temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBPOffsetToReg(
							context.getBasePointer(dest.index),
							context.getScratchRegister(),
							QUAD
						),
						createThisRegToArrayIndex(
							context.getRegister(source),
							dest.allocate(context.getRegister(dest.base), context.getScratchRegister())
						)
					);
				}
			} else {
				if (context.isRegister(dest.index)) {
					// index and source are registers; base is not
					// move the base to the temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBPOffsetToReg(
							context.getBasePointer(dest.base),
							context.getScratchRegister(),
							QUAD
						),
						createThisRegToArrayIndex(
							context.getRegister(source),
							dest.allocate(context.getScratchRegister(), context.getRegister(dest.index))
						)
					);
				} else {
					// source is a register, index and base are not
					// simply move the base pointer offsets into index and base
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
						createThisRegToArrayIndex(
							context.getRegister(source),
							dest.allocate(context.getScratchRegister(), context.getSecondScratch())
						)
					);
				}
			}
		} else {
			if (context.isRegister(dest.base)) {
				if (context.isRegister(dest.index)) {
					// base and index are registers, source is not
					// move source, tempImm; do the indexing operation with tempImm as the source
					return Arrays.asList(
						new MoveBPOffsetToReg(
							context.getBasePointer(source),
							context.getScratchRegister(),
							source.getSuffix()
						),
						createThisRegToArrayIndex(
							context.getScratchRegister(),
							dest.allocate(context.getRegister(dest.base), context.getRegister(dest.index))
						)
					);
				} else {
					// base is register, source and index are not.
					// move -8(rbp), (%base, -16(%rbp), scaling)

					// can move the source to scratch1
					// move the index to scratch2
					// move scratch1, (%base, %scratch2, scaling)
					return Arrays.asList(
						new MoveBPOffsetToReg(
							context.getBasePointer(source),
							context.getScratchRegister(),
							source.getSuffix()
						),
						new MoveBPOffsetToReg(
							context.getBasePointer(dest.index),
							context.getSecondScratch(),
							QUAD
						),
						createThisRegToArrayIndex(
							context.getScratchRegister(),
							dest.allocate(context.getRegister(dest.base), context.getSecondScratch())
						)
					);
				}
			} else {
				if (context.isRegister(dest.index)) {
					// index is register, source and base are not
					// move -8(rbp), (-16(%rbp), %index, scaling)

					// can move the source to scratch1
					// move the base to scratch2
					// move scratch1, (scratch2, %index, scaling)
					return Arrays.asList(
						new MoveBPOffsetToReg(
							context.getBasePointer(source),
							context.getScratchRegister(),
							source.getSuffix()
						),
						new MoveBPOffsetToReg(
							context.getBasePointer(dest.base),
							context.getSecondScratch(),
							QUAD
						),
						createThisRegToArrayIndex(
							context.getScratchRegister(),
							dest.allocate(context.getSecondScratch(), context.getRegister(dest.index))
						)
					);
				} else {

					// none of them are registers, they are all allocated to base pointer offsets.
					// op -8(%rbp), (-16(%rbp), -24(%rbp), scaling)
					return allocateNoRegs(context);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "\t" + opcode + " " + source + ", " + dest;
	}
}

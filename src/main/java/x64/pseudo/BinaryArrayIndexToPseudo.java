package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

public abstract class BinaryArrayIndexToPseudo implements PseudoInstruction {
	@NotNull
	final PseudoIndexing source;
	@NotNull
	final X64PseudoRegister destination;

	BinaryArrayIndexToPseudo(@NotNull PseudoIndexing source, @NotNull X64PseudoRegister destination) {
		this.source = source;
		this.destination = destination;
	}

	/**
	 * Creates the subclass's binary op source, offset(base, index, scaling)
	 * @param source The register allocated to the source.
	 * @param destination The register indexing destination.
	 * @return The subclass.
	 */
	abstract BinaryArrayIndexToReg
		createThisArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination);

	abstract BinaryRegToBPOffset
		createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination);

	abstract @NotNull List<@NotNull Instruction> allocateNoRegs(@NotNull AllocationContext context)
		throws NotSecondScratchException;

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// binary op source, offset(base, index, scaling)

		if (context.isRegister(source.base)) {
			if (context.isRegister(source.index)) {
				if (context.isRegister(destination)) {

					// simple case -- all are allocated to hardware ones
					return Collections.singletonList(
						createThisArrayIndexToReg(
							source.allocate(context.getRegister(source.base), context.getRegister(source.index)),
							context.getRegister(destination)
						)
					);

				} else {

					// slightly more complicated, have to first retrieve the memory at the indexing,
					//   then perform the operation
					return Arrays.asList(
						// move indexing operation, to scratch register
						new MoveArrayIndexToReg(
							source.allocate(context.getRegister(source.base), context.getRegister(source.index)),
							context.getScratchRegister(),
							destination.getSuffix()
						),
						// binary op with the scratch register, base pointer offset
						createThisRegToBPOffset(
							context.getScratchRegister(),
							context.getBasePointer(destination)
						)
					);
				}
			} else {
				if (context.isRegister(destination)) {
					// source.base is reg, so is destination. source.index is BPOffset
					return Arrays.asList(
						// load the index to a registers
						new MoveBPOffsetToReg(
							context.getBasePointer(source.index),
							context.getScratchRegister(),
							QUAD
						),
						// do the binary op with the array
						createThisArrayIndexToReg(
							source.allocate(context.getRegister(source.base), context.getScratchRegister()),
							context.getRegister(destination)
						)
					);
				} else {
					// source.base is reg, destination and source.index are BPOffsets
					return Arrays.asList(
						// move source.index to a scratch register 1
						new MoveBPOffsetToReg(
							context.getBasePointer(source.index),
							context.getScratchRegister(),
							QUAD
						),
						// move indexing operation, to scratch register 2
						new MoveArrayIndexToReg(
							source.allocate(context.getRegister(source.base), context.getScratchRegister()),
							context.getSecondScratch(),
							destination.getSuffix()
						),
						// binary op with the scratch register 2, base pointer offset
						createThisRegToBPOffset(
							context.getSecondScratch(),
							context.getBasePointer(destination)
						)
					);
				}
			}
		} else {
			if (context.isRegister(source.index)) {
				if (context.isRegister(destination)) {
					// source.base is BPOffset, source.index and destination are registers.
					return Arrays.asList(
						// load the source.base to register
						new MoveBPOffsetToReg(
							context.getBasePointer(source.base),
							context.getScratchRegister(),
							QUAD
						),
						// do the indexing operation
						createThisArrayIndexToReg(
							source.allocate(context.getRegister(source.base), context.getScratchRegister()),
							context.getRegister(destination)
						)
					);
				} else {
					// source.base and destination are BPOffsets, source.index is a register.
					return Arrays.asList(
						// move source.base to a scratch register 1
						new MoveBPOffsetToReg(
							context.getBasePointer(source.base),
							context.getScratchRegister(),
							QUAD
						),
						// move indexing operation, to scratch register 2
						new MoveArrayIndexToReg(
							source.allocate(context.getScratchRegister(), context.getRegister(source.index)),
							context.getSecondScratch(),
							destination.getSuffix()
						),
						// binary op with the scratch register 2, base pointer offset
						createThisRegToBPOffset(
							context.getSecondScratch(),
							context.getBasePointer(destination)
						)
					);
				}
			} else {
				if (context.isRegister(destination)) {
					// destination is register, source.base and source.index are BPOffsets
					// load the source.base to the first one and the source.index to the second one
					//  then do the array index to the register
					return Arrays.asList(
						// load the source.base to the first scratch register
						new MoveBPOffsetToReg(
							context.getBasePointer(source.base),
							context.getScratchRegister(),
							destination.getSuffix()
						),
						// load the source.index to the second register
						new MoveBPOffsetToReg(
							context.getBasePointer(source.index),
							context.getSecondScratch(),
							destination.getSuffix()
						),
						// do the indexing operation
						createThisArrayIndexToReg(
							source.allocate(context.getScratchRegister(), context.getSecondScratch()),
							context.getRegister(destination)
						)
					);
				} else {
					// none of the 3 are registers -- each subclass has its own way of doing this
					return allocateNoRegs(context);
				}
			}
		}
	}

	@Override
	public final void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source.base, i);
		usedRegs.markUsed(source.index, i);
		// defines the result
		usedRegs.markDefined(destination, i);
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveArrayIndexReg;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

/**
 * Represents mov source, (base, index, scaling).
 */
public class MoveArrayIndexPseudo implements PseudoInstruction {
	@NotNull private final X64PseudoRegister source, base, index;
	private final int scaling;

	public MoveArrayIndexPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister base,
								@NotNull X64PseudoRegister index, int scaling) {
		this.source = source;
		this.base = base;
		this.index = index;
		this.scaling = scaling;
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		// mov source, (base, index, scaling).
		if (context.isRegister(source)) {
			if (context.isRegister(base)) {
				if (context.isRegister(index)) {
					// all 3 are hardware registers, just a simple move
					return Collections.singletonList(
						new MoveArrayIndexReg(context.getRegister(source), context.getRegister(base), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source, base are regs, index is a base pointer offset
					// move the index to temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(context.getBasePointer(index), context.getScratchRegister(), QUAD),
						new MoveArrayIndexReg(context.getRegister(source), context.getRegister(base), context.getScratchRegister(),
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
						new MoveArrayIndexReg(context.getRegister(source), context.getScratchRegister(), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source is a register, index and base are not

				}
			}
		} else {
			if (context.isRegister(base)) {
				if (context.isRegister(index)) {
					// base and index are registers, source is not
					// move source, tempImm; do the indexing operation with tempImm as the source
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(context.getBasePointer(source), context.getScratchRegister(), source.getSuffix()),
						new MoveArrayIndexReg(context.getScratchRegister(), context.getRegister(base), context.getRegister(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// base is register, source and index are not.

				}
			} else {
				if (context.isRegister(index)) {
					// index is register, source and base are not
				} else {
					// none of them are registers, they are all allocated to base pointer offsets.
				}
			}
		}

		// TODO handle the 4 more complicated cases: 3 cases with 2 base pointers, and the case with all 3

		// actually due to the complexity of this instruction, we might want to have at least 2 scratch registers.
		//  this will involve first wanting to calculate the number of temporaries required by the instructions.
		// then a second pass will do the transformation.

		// also the subclasses of binary statements should just have a method that creates themselves, with
		//  binary instruction doing the allocation.
		// This way the allocation problem is only unique for each type of instruction, not based on the opcode.
		

		throw new RuntimeException("MoveArrayIndex allocation can't handle the 3 registers."
			+ context.isRegister(source) + context.isRegister(base) + context.isRegister(index) );
	}
}

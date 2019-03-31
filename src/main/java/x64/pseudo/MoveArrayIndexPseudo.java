package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveArrayIndexReg;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {

		// mov source, (base, index, scaling).
		if (mapping.containsKey(source)) {
			if (mapping.containsKey(base)) {
				if (mapping.containsKey(index)) {
					// all 3 are hardware registers, just a simple move
					return Collections.singletonList(
						new MoveArrayIndexReg(mapping.get(source), mapping.get(base), mapping.get(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source, base are regs, index is a base pointer offset
					// move the index to temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(locals.get(index), temporaryImmediate, QUAD),
						new MoveArrayIndexReg(mapping.get(source), mapping.get(base), temporaryImmediate,
							scaling, source.getSuffix()
						)
					);
				}
			} else {
				if (mapping.containsKey(index)) {
					// index and source are registers; base is not
					// move the base to the temp immediate, then do the indexing operation
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(locals.get(base), temporaryImmediate, QUAD),
						new MoveArrayIndexReg(mapping.get(source), temporaryImmediate, mapping.get(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// source is a register, index and base are not

				}
			}
		} else {
			if (mapping.containsKey(base)) {
				if (mapping.containsKey(index)) {
					// base and index are registers, source is not
					// move source, tempImm; do the indexing operation with tempImm as the source
					return Arrays.asList(
						new MoveBasePointerOffsetToReg(locals.get(source), temporaryImmediate, source.getSuffix()),
						new MoveArrayIndexReg(temporaryImmediate, mapping.get(base), mapping.get(index),
							scaling, source.getSuffix()
						)
					);
				} else {
					// base is register, source and index are not.

				}
			} else {
				if (mapping.containsKey(index)) {
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
			+ mapping.containsKey(source) + mapping.containsKey(base) + mapping.containsKey(index) );
	}
}

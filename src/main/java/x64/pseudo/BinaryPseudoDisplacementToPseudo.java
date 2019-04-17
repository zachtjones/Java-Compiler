package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.BinaryRegDisplacementToReg;
import x64.instructions.Instruction;
import x64.instructions.MoveBPOffsetToReg;
import x64.instructions.MoveRegToBPOffset;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BinaryPseudoDisplacementToPseudo implements PseudoInstruction {

	@NotNull public final PseudoDisplacement source;
	@NotNull public final X64PseudoRegister destination;
	@NotNull private final String name;


	BinaryPseudoDisplacementToPseudo(@NotNull String name, @NotNull PseudoDisplacement source,
											@NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	/** Creates the subclass of this op (register), register */
	abstract BinaryRegDisplacementToReg
		createThisRegDisplacementToReg(@NotNull RegDisplacement source, @NotNull X64Register destination);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		// example: mov 8(%q1), %q2
		if (context.isRegister(source.register)) {
			if (context.isRegister(destination)) {
				// mov 8(%r1), %r2
				return Collections.singletonList(
					createThisRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getRegister(source.register)),
						context.getRegister(destination)
					)
				);
			} else {
				// destination is mapped to base pointer offset
				// mov 8(%r1), -16(%rbp) -- use temporary in the middle
				return Arrays.asList(
					createThisRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getRegister(source.register)),
						context.getScratchRegister()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			// example: mov 8(%q1), %q2

			if (context.isRegister(destination)) {
				// mov 8(-16(%rbp)), %r2
				//  goes to:
				// mov -16(%rbp), %temp
				// mov 8(%temp), %r2
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.register),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					createThisRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getScratchRegister()),
						context.getRegister(destination)
					)
				);

			} else {
				// mov 8(-16(%rbp)), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// mov 8(%temp), %temp
				// mov %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source.register),
						context.getScratchRegister(),
						destination.getSuffix()
					),
					createThisRegDisplacementToReg(
						new RegDisplacement(source.offset, context.getScratchRegister()),
						context.getScratchRegister()
					),
					new MoveRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

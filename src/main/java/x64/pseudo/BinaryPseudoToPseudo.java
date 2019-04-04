package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/***
 * Represents a binary instruction that involves a pseudo register source and pseudo register destination.
 */
public abstract class BinaryPseudoToPseudo implements PseudoInstruction {

	@NotNull public final X64PseudoRegister source;
	@NotNull public final X64PseudoRegister destination;
	@NotNull private final String name;


	BinaryPseudoToPseudo(@NotNull String name, @NotNull X64PseudoRegister source,
								@NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source, i);
		usedRegs.markDefined(destination, i);
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}

	@NotNull
	abstract BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
											   @NotNull X64InstructionSize size);

	abstract BinaryRegToBasePointerOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BasePointerOffset destination, @NotNull X64InstructionSize size);
	abstract BinaryBasePointerOffsetToReg createThisBPOffsetToReg(@NotNull BasePointerOffset source, @NotNull X64Register destination, @NotNull X64InstructionSize size);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		// example: op %q1, %q2
		if (context.isRegister(source)) {
			if (context.isRegister(destination)) {
				// op %r1, %r2
				return Collections.singletonList(
					createThisRegToReg(
						context.getRegister(source),
						context.getRegister(destination),
						destination.getSuffix()
					)
				);
			} else {
				// op %r1, -16(%rbp)
				return Collections.singletonList(
					createThisRegToBPOffset(
						context.getRegister(source),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (context.isRegister(destination)) {
				// op -16(%rbp), %r2
				return Collections.singletonList(
					createThisBPOffsetToReg(
						context.getBasePointer(source),
						context.getRegister(destination),
						source.getSuffix())
				);
			} else {
				// op -16(%rbp), -24(%rbp)
				//  goes to:
				// mov -16(%rbp), %temp
				// op %temp, -24(%rbp)
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix()
					),
					createThisRegToBPOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

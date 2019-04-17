package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.NotSecondScratchException;
import x64.allocation.RegistersUsed;
import x64.instructions.BinaryRegToRegDisplacement;
import x64.instructions.Instruction;
import x64.instructions.MoveBPOffsetToReg;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static x64.X64InstructionSize.QUAD;

/***
 * Represents a binary instruction that involves a pseudo register source and displacement from a pseudo register.
 */
public abstract class BinaryPseudoToPseudoDisplacement implements PseudoInstruction {

	@NotNull public final X64PseudoRegister source;
	@NotNull public final PseudoDisplacement destination;
	@NotNull private final String name;


	BinaryPseudoToPseudoDisplacement(@NotNull String name, @NotNull X64PseudoRegister source,
											@NotNull PseudoDisplacement destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source, i);
	}

	/** Creates the subclass of this op register, offset(register) */
	abstract BinaryRegToRegDisplacement
		createThisRegToRegDisplacement(@NotNull X64Register source, @NotNull RegDisplacement destination);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context)
		throws NotSecondScratchException {

		// destination is always a memory operand, the displacement

		// an example is useful to think of for this one:
		// mov %q1, 8(%q2)
		if (context.isRegister(source)) {
			if (context.isRegister(destination.register)) {
				// %q1 = %r12, %q2 = %r13
				// becomes: mov %r12, 8(%r13)
				return Collections.singletonList(
					createThisRegToRegDisplacement(
						context.getRegister(source),
						new RegDisplacement(destination.offset, context.getRegister(destination.register))
					)
				);
			} else {
				// %q1 = %r12, %q2 = -16(%rbp)
				// would be: mov %r12, 8(16(%rbp)), which isn't possible
				// instead:
				//   mov 16(%rbp), %temp
				//   mov %r12, 8(%temp)
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(destination.register),
						context.getScratchRegister(),
						source.getSuffix()
					),
					createThisRegToRegDisplacement(
						context.getScratchRegister(),
						new RegDisplacement(destination.offset, context.getScratchRegister())
					)
				);
			}
		} else { // mov %q1, 8(%q2)
			if (context.isRegister(destination.register)) {
				// %q1 = -16(%rbp), %q2 = %r14
				// becomes:
				// mov -16(%rbp), 8(%r14), which can't be done
				// mov -16(%rbp), %temp; mov %temp, 8(%r14)
				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix()
					),
					createThisRegToRegDisplacement(
						context.getScratchRegister(),
						new RegDisplacement(destination.offset, context.getRegister(destination.register))
					)
				);
			} else {
				// %q1 = -24(%rbp), %q2 = -16(%rbp)
				//  mov -24(%rbp), 8(-16(%rbp))

				// want to do these 3 instructions (but uses 2 temps, which we don't have)
				//  mov -24(%rbp), %temp1  # source loaded
				//  mov -16(%rbp), %temp2  # destination loaded
				//  mov %temp1, 8(%temp2)

				return Arrays.asList(
					new MoveBPOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix()
					),
					new MoveBPOffsetToReg(
						context.getBasePointer(destination.register),
						context.getSecondScratch(),
						QUAD
					),
					createThisRegToRegDisplacement(
						context.getScratchRegister(),
						new RegDisplacement(destination.offset, context.getSecondScratch())
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

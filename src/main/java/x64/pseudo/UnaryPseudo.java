package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Collections;
import java.util.List;

/***
 * Represents a unary instruction that involves a pseudo register.
 */
public abstract class UnaryPseudo implements PseudoInstruction {

	@NotNull public final X64PseudoRegister operand;
	@NotNull private final String name;


	UnaryPseudo(@NotNull String name, @NotNull X64PseudoRegister operand) {
		this.name = name;
		this.operand = operand;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(operand, i);
		usedRegs.markDefined(operand, i);
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			operand.toString();
	}

	@NotNull
	abstract UnaryReg createThisReg(@NotNull X64Register operand);

	@NotNull
	abstract UnaryBPOffset createThisBPOffset(@NotNull BPOffset operand);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		// since there's only one operand, this makes it pretty easy
		if (context.isRegister(operand)) {
			return Collections.singletonList(
				createThisReg(
					context.getRegister(operand)
				)
			);
		} else {
			return Collections.singletonList(
				createThisBPOffset(
					context.getBasePointer(operand)
				)
			);
		}
	}
}

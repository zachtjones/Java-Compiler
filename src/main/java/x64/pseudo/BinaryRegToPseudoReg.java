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
 * Represents a binary instruction that involves a pseudo register source and native register destination.
 */
public abstract class BinaryRegToPseudoReg implements PseudoInstruction {
	@NotNull
	public final X64Register source;
	@NotNull
	public final X64PseudoRegister destination;
	private final String name;


	BinaryRegToPseudoReg(String name, @NotNull X64Register source, @NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	@NotNull
	abstract BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination);

	@NotNull
	abstract BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination);

	@Override
	public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		if (context.isRegister(destination)) {
			return Collections.singletonList(
				createThisRegToReg(
					source,
					context.getRegister(destination)
				)
			);
		} else {
			return Collections.singletonList(
				createThisRegToBPOffset(
					source,
					context.getBasePointer(destination)
				)
			);
		}
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

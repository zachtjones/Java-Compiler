package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegistersUsed;
import x64.operands.PseudoAbsolute;
import x64.operands.X64PseudoRegister;

/***
 * Represents a binary instruction that involves a memory at pseudo register source and pseudo register destination.
 */
public abstract class BinaryPseudoAbsoluteToPseudo implements PseudoInstruction {
	@NotNull public final PseudoAbsolute source;
	@NotNull public final X64PseudoRegister destination;
	private final String name;


	public BinaryPseudoAbsoluteToPseudo(String name, @NotNull PseudoAbsolute source,
										@NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

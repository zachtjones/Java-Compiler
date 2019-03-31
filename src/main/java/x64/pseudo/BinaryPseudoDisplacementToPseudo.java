package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.PseudoDisplacement;
import x64.operands.X64PseudoRegister;

import java.util.Map;

public abstract class BinaryPseudoDisplacementToPseudo implements PseudoInstruction {

	@NotNull public final PseudoDisplacement source;
	@NotNull public final X64PseudoRegister destination;
	@NotNull private final String name;


	public BinaryPseudoDisplacementToPseudo(@NotNull String name, @NotNull PseudoDisplacement source,
											@NotNull X64PseudoRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	@Override
	public void prioritizeRegisters(Map<X64PseudoRegister, RegisterMapped> mapping) {
		context.getRegister(destination).increment();
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

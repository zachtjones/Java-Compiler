package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.PseudoDisplacement;
import x64.operands.X64PseudoRegister;

import java.util.Map;

/***
 * Represents a binary instruction that involves a pseudo register source and displacement from a pseudo register.
 */
public abstract class BinaryPseudoToPseudoDisplacement implements PseudoInstruction {

	@NotNull public final X64PseudoRegister source;
	@NotNull public final PseudoDisplacement destination;
	@NotNull private final String name;


	public BinaryPseudoToPseudoDisplacement(@NotNull String name, @NotNull X64PseudoRegister source,
											@NotNull PseudoDisplacement destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markUsed(source, i);
	}

	@Override
	public void prioritizeRegisters(Map<X64PseudoRegister, RegisterMapped> mapping) {
		context.getRegister(source).increment();
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

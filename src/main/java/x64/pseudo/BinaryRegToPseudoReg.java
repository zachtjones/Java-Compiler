package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

/***
 * Represents a binary instruction that involves a pseudo register source and native register destination.
 */
public abstract class BinaryRegToPseudoReg implements PseudoInstruction {
	@NotNull
	public final X64NativeRegister source;
	@NotNull
	public final X64PreservedRegister destination;
	private final String name;


	public BinaryRegToPseudoReg(String name, @NotNull X64NativeRegister source,
								@NotNull X64PreservedRegister destination) {
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		usedRegs.markDefined(destination, i);
	}

	@Override
	public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
		mapping.get(destination).increment();
	}

	/** Represents how this instruction should be represented */
	@Override
	public final String toString() {
		return '\t' + name + " " +
			source.toString() + ", " + destination.toString();
	}
}

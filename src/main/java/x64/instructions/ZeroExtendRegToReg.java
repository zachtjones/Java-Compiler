package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class ZeroExtendRegToReg extends BinaryRegToReg {

	private final String rep; // how this is represented

	public ZeroExtendRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
							  @NotNull X64InstructionSize sourceSize, @NotNull X64InstructionSize destinationSize) {

		super("movz", source, destination, sourceSize);

		// example: movzbl %al, %edx
		this.rep = "\tmovz" + sourceSize + destinationSize + " " +
			source.assemblyRep(sourceSize) + ", " + destination.assemblyRep(destinationSize);
	}

	@Override
	public String assemblyRepresentation() {
		return rep;
	}
}

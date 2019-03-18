package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class ZeroExtendBasePointerOffsetToReg extends Instruction {

	private final String rep; // how this is represented

	public ZeroExtendBasePointerOffsetToReg(@NotNull BasePointerOffset source, @NotNull X64Register destination,
											@NotNull X64InstructionSize sourceSize,
											@NotNull X64InstructionSize destinationSize) {

		// example: movsbq %al, -8(%rbp) -- byte to quad word
		this.rep = "\tmovz" + sourceSize + destinationSize + " " +
			source + ", " + destination.assemblyRep(destinationSize);
	}

	@Override
	public String assemblyRepresentation() {
		return rep;
	}
}

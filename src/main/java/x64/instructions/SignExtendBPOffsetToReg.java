package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class SignExtendBPOffsetToReg extends Instruction {

	private final String rep; // how this is represented

	public SignExtendBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination,
								   @NotNull X64InstructionSize sourceSize,
								   @NotNull X64InstructionSize destinationSize) {

		// example: movsbq %al, -8(%rbp) -- byte to quad word
		this.rep = "\tmovs" + sourceSize + destinationSize + " " +
			source + ", " + destination.assemblyRep(destinationSize);
	}

	@Override
	public String assemblyRepresentation() {
		return rep;
	}
}

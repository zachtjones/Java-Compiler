package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class SignExtendRegToReg extends Instruction {

	/** Represents a sign extension of a register to another register. */
	public SignExtendRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
							  @NotNull X64InstructionSize sourceSize, @NotNull X64InstructionSize destinationSize) {

		// example: movsbl %al, %edx
		super("\tmovs" + sourceSize + destinationSize + " " +
			source.assemblyRep(sourceSize) + ", " + destination.assemblyRep(destinationSize));
	}
}

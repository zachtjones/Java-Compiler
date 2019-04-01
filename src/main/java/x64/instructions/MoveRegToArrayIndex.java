package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class MoveRegToArrayIndex extends Instruction {

	@NotNull private final String rep;

	public MoveRegToArrayIndex(@NotNull X64Register source, @NotNull X64Register base, @NotNull X64Register index,
							   int scaling, @NotNull X64InstructionSize size) {
		// base, index are quads
		rep = "\tmov" + size + " " + source.assemblyRep(size) + ", (" + base + ", " + index + ", " + scaling + ")";
	}

	@Override
	public String assemblyRepresentation() {
		return rep;
	}
}

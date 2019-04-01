package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class PopArrayIndex extends Instruction {

	private final String rep;

	public PopArrayIndex(@NotNull X64Register base, @NotNull X64Register index, int scaling) {
		rep = "popq (" + base + ", " + index + ", " + scaling + ")";
	}

	@Override
	public String assemblyRepresentation() {
		return rep;
	}
}

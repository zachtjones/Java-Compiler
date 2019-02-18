package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class PopNativeReg extends Instruction {

	private X64Register reg;

	public PopNativeReg(@NotNull X64Register reg) {
		this.reg = reg;
	}

	@Override
	public String assemblyRepresentation() {
		return "\tpopq " + reg;
	}
}

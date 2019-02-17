package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

public class PopNativeRegInstruction extends Instruction {

	private X64NativeRegister reg;

	public PopNativeRegInstruction(@NotNull X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public String assemblyRepresentation() {
		return "\tpopq " + reg;
	}
}

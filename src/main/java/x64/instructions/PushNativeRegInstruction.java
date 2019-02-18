package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

/**
 * Represents a push of a native register to the stack, usually in the function prologue.
 */
public class PushNativeRegInstruction extends Instruction {

	private X64NativeRegister reg;

	public PushNativeRegInstruction(@NotNull X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public String assemblyRepresentation() {
		return "\tpushq " + reg;
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.Instruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a push of a native register to the stack, usually in the function prologue.
 */
public class PushNativeRegInstruction implements Instruction {

	private X64NativeRegister reg;

	public PushNativeRegInstruction(@NotNull X64NativeRegister reg) {
		this.reg = reg;
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {
		return Collections.singletonList(this);
	}

	@Override
	public String toString() {
		return "\tpushq " + reg;
	}
}

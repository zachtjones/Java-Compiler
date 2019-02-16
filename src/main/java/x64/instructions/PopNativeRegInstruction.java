package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.Instruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PopNativeRegInstruction implements Instruction {

	private X64NativeRegister reg;

	public PopNativeRegInstruction(@NotNull X64NativeRegister reg) {
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
		return "\tpopq " + reg;
	}
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

public class MoveImmToRegInstruction extends BinaryImmRegInstruction {


	public MoveImmToRegInstruction(@NotNull Immediate source,
								   @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

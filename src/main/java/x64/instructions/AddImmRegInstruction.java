package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

/** Represents a add $number, %register */
public class AddImmRegInstruction extends BinaryImmRegInstruction {

	public AddImmRegInstruction(@NotNull Immediate source, @NotNull X64NativeRegister destination) {
		super("sub", source, destination);
	}
}

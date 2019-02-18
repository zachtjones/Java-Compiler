package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64NativeRegister;

public class MoveRIPRelativeToReg extends BinaryRIPRelativeToRegInstruction {
	public MoveRIPRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

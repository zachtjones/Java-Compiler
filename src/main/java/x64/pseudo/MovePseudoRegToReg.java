package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

public class MovePseudoRegToReg extends BinaryInstructionPseudoRegToReg {

	public MovePseudoRegToReg(@NotNull X64PreservedRegister source,
							  @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

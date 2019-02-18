package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.X64NativeRegister;

/** Like movq (%r10), %r11 */
public class MoveRegAbsoluteToReg extends BinaryAbsoluteRegToRegInstruction {

	public MoveRegAbsoluteToReg(@NotNull X64NativeRegister source,
								@NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}
}

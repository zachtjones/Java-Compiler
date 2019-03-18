package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** Like movq (%r10), %r11 */
public class MoveRegAbsoluteToReg extends BinaryAbsoluteRegToReg {

	public MoveRegAbsoluteToReg(@NotNull X64Register source, @NotNull X64Register destination,
								@NotNull X64InstructionSize size) {
		super("mov", source, destination, size);
	}
}

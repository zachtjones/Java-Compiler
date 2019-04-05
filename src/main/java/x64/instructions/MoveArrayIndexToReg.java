package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegIndexing;
import x64.operands.X64Register;

/** Represents mov offset(base, index, scaling), destination. */
public class MoveArrayIndexToReg extends BinaryArrayIndexToReg {

	/** Represents mov offset(base, index, scaling), destination. */
	public MoveArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination,
							   @NotNull X64InstructionSize size) {

		super("mov", source, destination, size);
	}
}

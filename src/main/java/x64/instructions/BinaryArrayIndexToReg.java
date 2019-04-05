package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegIndexing;
import x64.operands.X64Register;

public class BinaryArrayIndexToReg extends Instruction {

	BinaryArrayIndexToReg(@NotNull String opcode, @NotNull RegIndexing source,
						  @NotNull X64Register destination, @NotNull X64InstructionSize size) {

		super("\t" + opcode + size + " " + source + ", " + destination.assemblyRep(size));
	}
}

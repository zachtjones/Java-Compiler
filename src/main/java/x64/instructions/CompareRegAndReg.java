package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

public class CompareRegAndReg extends Instruction{

    /** Represents a comparison of two registers. */
    public CompareRegAndReg(@NotNull X64Register src1, @NotNull X64Register src2, @NotNull X64InstructionSize size) {
        // note the reversed syntax from what we would expect.
		super("\tcmp" + size + " " + src2.assemblyRep(size) + ", " + src1.assemblyRep(size));
    }
}

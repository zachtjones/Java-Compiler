package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.Immediate;
import x64.operands.X64Register;

public class CompareRegAndImm extends Instruction {

    /** Represents a compare of the register and immediate value. */
    public CompareRegAndImm(@NotNull X64Register src1, @NotNull Immediate src2, @NotNull X64InstructionSize size) {
		super("\tcmp" + size + " " + src2 + ", " + src1.assemblyRep(size));
    }
}

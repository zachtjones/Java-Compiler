package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class CompareRegAndBPOffset extends Instruction {

    /** Represents a compare of a register and the memory at a base pointer offset. */
    public CompareRegAndBPOffset(@NotNull X64Register src1, @NotNull BPOffset src2,
                                 @NotNull X64InstructionSize size) {

		super("\tcmp" + size + " " + src2 + ", " + src1.assemblyRep(size));
    }
}

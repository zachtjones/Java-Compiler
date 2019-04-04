package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class CompareRegAndBPOffset extends Instruction {
    @NotNull private final X64InstructionSize size;
    @NotNull private final X64Register src1;
    @NotNull private final BPOffset src2;

    public CompareRegAndBPOffset(@NotNull X64Register src1, @NotNull BPOffset src2,
                                 @NotNull X64InstructionSize size) {
        this.src1 = src1;
        this.src2 = src2;
        this.size = size;
    }

    @Override
    public String assemblyRepresentation() {
        // reversed syntax with at&t style.
        return "\tcmp" + size + " " + src2 + ", " + src1.assemblyRep(size);
    }
}

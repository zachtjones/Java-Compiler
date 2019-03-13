package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class CompareRegAndBasePointerOffset extends Instruction {
    @NotNull private final X64InstructionSize size;
    @NotNull private final X64Register src1;
    @NotNull private final BasePointerOffset src2;

    public CompareRegAndBasePointerOffset(@NotNull X64Register src1, @NotNull BasePointerOffset src2,
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

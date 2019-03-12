package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class CompareBasePointerOffsetAndReg extends Instruction{
    @NotNull private final BasePointerOffset src1;
    @NotNull private final X64Register src2;

    public CompareBasePointerOffsetAndReg(@NotNull BasePointerOffset src1, @NotNull X64Register src2) {
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public String assemblyRepresentation() {
        // reversed syntax with at&t style.
        return "cmp" + src2.getSuffix() + "\t" + src2 + ", " + src1;
    }
}

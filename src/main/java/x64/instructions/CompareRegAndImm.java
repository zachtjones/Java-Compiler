package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64Register;

public class CompareRegAndImm extends Instruction {
    @NotNull private X64Register src1;
    @NotNull private Immediate src2;

    public CompareRegAndImm(@NotNull X64Register src1, @NotNull Immediate src2) {
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public String assemblyRepresentation() {
        return "cmp" + src1.getSuffix() + "\t" + src2 + ", " + src1;
    }
}

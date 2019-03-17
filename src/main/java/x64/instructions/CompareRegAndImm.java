package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.Immediate;
import x64.operands.X64Register;

public class CompareRegAndImm extends Instruction {
    @NotNull private final X64InstructionSize size;
    @NotNull private X64Register src1;
    @NotNull private Immediate src2;

    public CompareRegAndImm(@NotNull X64Register src1, @NotNull Immediate src2, @NotNull X64InstructionSize size) {
        this.src1 = src1;
        this.src2 = src2;
        this.size = size;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tcmp" + size + " " + src2 + ", " + src1.assemblyRep(size);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.Immediate;

public class CompareBPOffsetAndImm extends Instruction{
    @NotNull private final BPOffset src1;
    @NotNull private final Immediate src2;
    @NotNull private X64InstructionSize size;

    public CompareBPOffsetAndImm(@NotNull BPOffset src1, @NotNull Immediate src2,
                                 @NotNull X64InstructionSize size) {
        this.src1 = src1;
        this.src2 = src2;
        this.size = size;
    }

    @Override
    public String assemblyRepresentation() {
        // reversed syntax with at&t style.
        return "\tcmp" + size + " " + src2 + ", " + src1;
    }
}

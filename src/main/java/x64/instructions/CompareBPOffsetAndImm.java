package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.Immediate;

public class CompareBPOffsetAndImm extends Instruction{

    /** Compares the memory at the base pointer offset and the immediate value. */
    public CompareBPOffsetAndImm(@NotNull BPOffset src1, @NotNull Immediate src2,
                                 @NotNull X64InstructionSize size) {

        // reversed syntax with at&t style.
        super("\tcmp" + size + " " + src2 + ", " + src1);
    }
}

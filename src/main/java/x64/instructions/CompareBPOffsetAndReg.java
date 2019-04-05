package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

public class CompareBPOffsetAndReg extends Instruction{

    /** Compares the memory at the base pointer offset and the register. */
    public CompareBPOffsetAndReg(@NotNull BPOffset src1, @NotNull X64Register src2,
                                 @NotNull X64InstructionSize size) {
		super("\tcmp" + size + " " + src2.assemblyRep(size) + ", " + src1);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;

/** Represents the 2's complement of memory at an offset of a base pointer (0 - the operand) */
public class NegationBPOffset extends UnaryBPOffset {
    public NegationBPOffset(@NotNull BPOffset operand, X64InstructionSize suffix) {
        super("neg", operand, suffix);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;

/** Represents the not operation on an offset from the base pointer register */
public class NotBPOffset extends UnaryBPOffset {
    public NotBPOffset(@NotNull BPOffset operand, X64InstructionSize suffix) {
        super("not", operand, suffix);
    }
}

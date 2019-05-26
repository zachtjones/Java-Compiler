package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.Immediate;

/** Represents an xor of an immediate an a base pointer offset */
public class XorImmToBPOffset extends BinaryImmToBPOffset {
    public XorImmToBPOffset(@NotNull Immediate source, @NotNull BPOffset destination, X64InstructionSize suffix) {
        super("xor", source, destination, suffix);
    }
}

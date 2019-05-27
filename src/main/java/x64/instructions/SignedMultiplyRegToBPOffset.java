package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** A signed multiply of source * destination -> destination,
 * where both the source is a register and the destination is a base pointer offset */
public class SignedMultiplyRegToBPOffset extends BinaryRegToBPOffset {
    public SignedMultiplyRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination,
                                       @NotNull X64InstructionSize suffix) {

        super("imul", source, destination, suffix);
    }
}

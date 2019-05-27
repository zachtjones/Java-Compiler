package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** A signed multiply of source * destination -> destination,
 * where the source is a base pointer offset, and the destination is a register. */
public class SignedMultiplyBPOffsetToReg extends BinaryBPOffsetToReg {
    public SignedMultiplyBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination,
                                       @NotNull X64InstructionSize suffix) {

        super("imul", source, destination, suffix);
    }
}

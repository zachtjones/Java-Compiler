package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** A signed multiply of source * destination -> destination, where both operands are registers */
public class SignedMultiplyRegToReg extends BinaryRegToReg {
    public SignedMultiplyRegToReg(@NotNull X64Register source, @NotNull X64Register destination,
                                  @NotNull X64InstructionSize suffix) {

        super("imul", source, destination, suffix);
    }
}

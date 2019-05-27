package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;

/** Represents a signed division with a base pointer divisor, DX:AX are used for the dividend */
public class SignedDivisionBPOffset extends Instruction {
    public SignedDivisionBPOffset(@NotNull BPOffset basePointer, @NotNull X64InstructionSize suffix) {
        super("\tidiv" + suffix.size + " " + basePointer.toString() );
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

public class LoadEffectiveAddressRIPRelativeToReg extends BinaryRIPRelativeToReg {

    /** Represents a load effective address of offset(%rip), %register */
    public LoadEffectiveAddressRIPRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64Register destination,
                                                @NotNull X64InstructionSize size) {
        super("lea", source, destination, size);
    }
}

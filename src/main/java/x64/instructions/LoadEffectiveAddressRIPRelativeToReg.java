package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64NativeRegister;

public class LoadEffectiveAddressRIPRelativeToReg extends BinaryRIPRelativeToReg {

    /** Represents a load effective address of offset(%rip), %register */
    public LoadEffectiveAddressRIPRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64NativeRegister destination) {
        super("lea", source, destination);
    }
}

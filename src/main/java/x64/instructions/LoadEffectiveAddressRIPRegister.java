package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64NativeRegister;

public class LoadEffectiveAddressRIPRegister extends BinaryRIPRegisterInstruction {

    /** Represents a load effective address of offset(%rip), %register */
    public LoadEffectiveAddressRIPRegister(@NotNull RIPRelativeData source, @NotNull X64NativeRegister destination) {
        super("lea", source, destination);
    }
}

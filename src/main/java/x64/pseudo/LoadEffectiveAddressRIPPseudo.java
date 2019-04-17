package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRIPRelativeToReg;
import x64.instructions.LoadEffectiveAddressRIPRelativeToReg;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class LoadEffectiveAddressRIPPseudo extends BinaryRIPRelativeToPseudo {

    public LoadEffectiveAddressRIPPseudo(@NotNull RIPRelativeData source, @NotNull X64PseudoRegister destination) {
        super("leaq", source, destination);
    }

    @Override
    @NotNull BinaryRIPRelativeToReg createThisRipRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64Register destination) {
        return new LoadEffectiveAddressRIPRelativeToReg(source, destination, this.destination.getSuffix());
    }
}

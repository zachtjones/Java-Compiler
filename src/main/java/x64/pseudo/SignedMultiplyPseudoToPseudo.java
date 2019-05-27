package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

/** Represents a signed multiply instruction (imul), of source * destination -> destination */
public class SignedMultiplyPseudoToPseudo extends BinaryPseudoToPseudo {
    public SignedMultiplyPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
        super("imul", source, destination);
    }

    @Override
    @NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
        return new SignedMultiplyRegToReg(source, destination, this.destination.getSuffix());
    }

    @Override
    @NotNull BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
        return new SignedMultiplyRegToBPOffset(source, destination, this.destination.getSuffix());
    }

    @Override
    @NotNull BinaryBPOffsetToReg createThisBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination) {
        return new SignedMultiplyBPOffsetToReg(source, destination, this.destination.getSuffix());
    }
}

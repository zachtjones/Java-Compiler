package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryImmToBPOffset;
import x64.instructions.BinaryImmToReg;
import x64.instructions.XorImmToBPOffset;
import x64.instructions.XorImmToReg;
import x64.operands.BPOffset;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

/** Represents an xor of an immediate and a pseudo register */
public class XorImmToPseudo extends BinaryImmToPseudo {
    public XorImmToPseudo(Immediate source, X64PseudoRegister destination) {
        super("xor", source, destination);
    }

    @Override
    BinaryImmToReg createThisImmToReg(@NotNull Immediate source, @NotNull X64Register destination) {
        return new XorImmToReg(source, destination, this.destination.getSuffix());
    }

    @Override
    BinaryImmToBPOffset createThisImmToBPOffset(@NotNull Immediate source, @NotNull BPOffset destination) {
        return new XorImmToBPOffset(source, destination, this.destination.getSuffix());
    }
}

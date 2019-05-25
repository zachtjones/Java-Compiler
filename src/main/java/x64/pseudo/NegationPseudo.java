package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.NegationBPOffset;
import x64.instructions.NegationReg;
import x64.instructions.UnaryBPOffset;
import x64.instructions.UnaryReg;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class NegationPseudo extends UnaryPseudo {

    public NegationPseudo(X64PseudoRegister operand) {
        super("neg", operand);
    }

    @Override
    @NotNull UnaryReg createThisReg(@NotNull X64Register operand) {
        return new NegationReg(
            operand,
            this.operand.getSuffix()
        );
    }

    @Override
    @NotNull UnaryBPOffset createThisBPOffset(@NotNull BPOffset operand) {
        return new NegationBPOffset(
            operand,
            this.operand.getSuffix()
        );
    }
}

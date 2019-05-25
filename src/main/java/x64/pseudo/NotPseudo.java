package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class NotPseudo extends UnaryPseudo {

    /** Creates a not (1's complement) of the register operand */
    public NotPseudo(X64PseudoRegister operand) {
        super("not", operand);
    }

    @Override
    @NotNull UnaryReg createThisReg(@NotNull X64Register operand) {
        return new NotReg(
            operand,
            this.operand.getSuffix()
        );
    }

    @Override
    @NotNull UnaryBPOffset createThisBPOffset(@NotNull BPOffset operand) {
        return new NotBPOffset(
            operand,
            this.operand.getSuffix()
        );
    }
}

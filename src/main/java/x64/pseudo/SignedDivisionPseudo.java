package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.Instruction;
import x64.instructions.SignedDivisionBPOffset;
import x64.instructions.SignedDivisionRegister;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;

/** Divides DX:AX by the source parameter, storing the quotient in AX, remainder in DX */
public class SignedDivisionPseudo implements PseudoInstruction {
    private final X64PseudoRegister source;

    public SignedDivisionPseudo(@NotNull X64PseudoRegister source) {
        this.source = source;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(source, i);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

        if (context.isRegister(source)) {
            return Collections.singletonList(new SignedDivisionRegister(
                context.getRegister(source),
                source.getSuffix()
            ));
        } else {
            return Collections.singletonList(new SignedDivisionBPOffset(
                context.getBasePointer(source),
                source.getSuffix()
            ));
        }
    }
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.CompareBPOffsetAndImm;
import x64.instructions.CompareRegAndImm;
import x64.instructions.Instruction;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;

public class ComparePseudoAndImmediate implements PseudoInstruction {

    @NotNull private final X64PseudoRegister src1;
    @NotNull private final Immediate src2;

    public ComparePseudoAndImmediate(@NotNull X64PseudoRegister src1, @NotNull Immediate src2) {

        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(src1, i);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
        if (context.isRegister(src1)) {
            return Collections.singletonList(
                new CompareRegAndImm(context.getRegister(src1), src2, src1.getSuffix())
            );
        } else {
            return Collections.singletonList(
                new CompareBPOffsetAndImm(context.getBasePointer(src1), src2, src1.getSuffix())
            );
        }
    }

    @Override
    public String toString() {
        // compare has reversed order in at&t to what you expect
        return "\tcmp" + src1.getSuffix() + " " + src2 + ", " + src1;
    }
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComparePseudoPseudo implements PseudoInstruction {

    @NotNull private final X64PseudoRegister src1;
    @NotNull private final X64PseudoRegister src2;

    public ComparePseudoPseudo(@NotNull X64PseudoRegister src1, @NotNull X64PseudoRegister src2) {
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(src1, i);
        usedRegs.markUsed(src2, i);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

        if (context.isRegister(src1)) {
            if (context.isRegister(src2)) {
                // both get mapped to hardware
                return Collections.singletonList(
                    new CompareRegAndReg(
                        context.getRegister(src1),
                        context.getRegister(src2),
                        src1.getSuffix()
                    )
                );
            } else {
                // first is reg, second is base pointer offset
                return Collections.singletonList(
                    new CompareRegAndBasePointerOffset(
                        context.getRegister(src1),
                        context.getBasePointer(src2),
                        src1.getSuffix()
                    )
                );
            }
        } else {
            if (context.isRegister(src2)) {
                // first is base pointer offset, second is reg
                return Collections.singletonList(
                    new CompareBasePointerOffsetAndReg(
                        context.getBasePointer(src1),
                        context.getRegister(src2),
                        src1.getSuffix()
                    )
                );
            } else {
                // move first to the temp, compare temp and src2
                return Arrays.asList(
                    new MoveBasePointerOffsetToReg(
                        context.getBasePointer(src1),
                        context.getScratchRegister(),
                        src1.getSuffix()
                    ),
                    new CompareRegAndBasePointerOffset(
                        context.getScratchRegister(),
                        context.getBasePointer(src2),
                        src1.getSuffix()
                    )
                );
            }
        }
    }

    @Override
    public String toString() {
        // compare has reversed order in at&t to what you expect
        return "\tcmp" + src1.getSuffix() + " " + src2 + ", " + src1;
    }
}

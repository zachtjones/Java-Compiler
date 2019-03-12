package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ComparePseudoPseudo implements PseudoInstruction {

    @NotNull private final X64PseudoRegister src1;
    @NotNull private final X64PseudoRegister src2;

    public ComparePseudoPseudo(@NotNull X64PseudoRegister src1, @NotNull X64PseudoRegister src2) {
        this.src1 = src1;
        this.src2 = src2;
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
                                                        @NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
                                                        @NotNull X64Register temporaryImmediate) {

        if (mapping.containsKey(src1)) {
            if (mapping.containsKey(src2)) {
                // both get mapped to hardware
                return Collections.singletonList(
                    new CompareRegAndReg(
                        mapping.get(src1),
                        mapping.get(src2)
                    )
                );
            } else {
                // first is reg, second is base pointer offset
                return Collections.singletonList(
                    new CompareRegAndBasePointerOffset(
                        mapping.get(src1),
                        locals.get(src2)
                    )
                );
            }
        } else {
            if (mapping.containsKey(src2)) {
                // first is base pointer offset, second is reg
                return Collections.singletonList(
                    new CompareBasePointerOffsetAndReg(
                        locals.get(src1),
                        mapping.get(src2)
                    )
                );
            } else {
                // move first to the temp, compare temp and src2
                return Arrays.asList(
                    new MoveBasePointerOffsetToReg(
                        locals.get(src1),
                        temporaryImmediate
                    ),
                    new CompareRegAndBasePointerOffset(
                        temporaryImmediate,
                        locals.get(src2)
                    )
                );
            }
        }
    }
}

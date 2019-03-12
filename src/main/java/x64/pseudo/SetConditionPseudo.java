package x64.pseudo;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.SetConditionBasePointerOffset;
import x64.instructions.SetConditionReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SetConditionPseudo implements PseudoInstruction {
    @NotNull private final ConditionCode type;
    @NotNull private final X64PseudoRegister destination;

    public SetConditionPseudo(@NotNull ConditionCode type, @NotNull X64PseudoRegister destination) {
        this.type = type;
        this.destination = destination;
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
                                                        @NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
                                                        @NotNull X64Register temporaryImmediate) {

        if (mapping.containsKey(destination)) {
            return Collections.singletonList(
                new SetConditionReg(type, mapping.get(destination))
            );
        } else {
            return Collections.singletonList(
                new SetConditionBasePointerOffset(type, locals.get(destination))
            );
        }
    }
}

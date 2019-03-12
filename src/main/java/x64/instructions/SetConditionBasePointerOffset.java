package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;

public class SetConditionBasePointerOffset extends Instruction {
    @NotNull private final ConditionCode type;
    @NotNull private final BasePointerOffset offset;

    public SetConditionBasePointerOffset(@NotNull ConditionCode type, @NotNull BasePointerOffset offset) {
        this.type = type;
        this.offset = offset;
    }

    @Override
    public String assemblyRepresentation() {
        return "set" + type.x64Code() + "\t" + offset;
    }
}

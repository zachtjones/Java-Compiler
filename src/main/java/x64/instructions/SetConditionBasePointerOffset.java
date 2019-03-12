package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;

/** If the condition passes, makes the register hold the value 1, otherwise 0.*/
public class SetConditionBasePointerOffset extends Instruction {
    @NotNull private final ConditionCode type;
    @NotNull private final BasePointerOffset offset;

    public SetConditionBasePointerOffset(@NotNull ConditionCode type, @NotNull BasePointerOffset offset) {
        this.type = type;
        this.offset = offset;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tset" + type.x64Code() + " " + offset;
    }
}

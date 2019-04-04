package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;

/** If the condition passes, makes the register hold the value 1, otherwise 0.*/
public class SetConditionBPOffset extends Instruction {
    @NotNull private final ConditionCode type;
    @NotNull private final BPOffset offset;

    public SetConditionBPOffset(@NotNull ConditionCode type, @NotNull BPOffset offset) {
        this.type = type;
        this.offset = offset;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tset" + type.x64Code() + " " + offset;
    }
}

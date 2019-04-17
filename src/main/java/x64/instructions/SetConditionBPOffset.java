package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.BPOffset;

/** If the condition passes, makes the register hold the value 1, otherwise 0.*/
public class SetConditionBPOffset extends Instruction {

    /** Represents a statement that sets the memory at the base pointer to 1 if the condition holds, otherwise 0. */
    public SetConditionBPOffset(@NotNull ConditionCode type, @NotNull BPOffset offset) {
		super("\tset" + type.x64Code() + " " + offset);
    }
}

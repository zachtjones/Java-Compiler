package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;

/** Represents a jump to a label, provided the condition holds true (using E_FLAGS register) */
public class JumpConditionInstruction extends Instruction {

    /**
     * Represents a conditional jump statement to a label.
     * @param code The conditional code.
     * @param name The label to jump to if the condition holds (is true).
     */
    public JumpConditionInstruction(@NotNull ConditionCode code, @NotNull String name) {
		super("\tj" + code.x64Code() + "\t" + name);
    }
}

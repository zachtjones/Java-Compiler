package x64.instructions;

import org.jetbrains.annotations.NotNull;

/** Represents an unconditional jump to a label */
public class JumpInstruction extends Instruction {

	/**
	 * Represents a unconditional jump to the label.
	 * @param name The label to jump to.
	 */
    public JumpInstruction(@NotNull String name) {
		super("\tjmp\t" + name);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;

/** Represents a pop off the stack into the memory displacement of the register. */
public class PopDisplacement extends Instruction {

    public PopDisplacement(@NotNull RegDisplacement displacement) {
		super("pop\t" + displacement);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

/** Represents a pop off the stack into the memory displacement of the register. */
public class PopDisplacement extends Instruction {
    @NotNull private final RegDisplacement displacement;

    public PopDisplacement(@NotNull RegDisplacement displacement) {
        this.displacement = displacement;
    }

    @Override
    public String assemblyRepresentation() {
        return "pop\t" + displacement;
    }
}

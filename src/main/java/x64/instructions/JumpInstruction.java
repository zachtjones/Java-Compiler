package x64.instructions;

import org.jetbrains.annotations.NotNull;

/** Represents an unconditional jump to a label */
public class JumpInstruction extends Instruction {

    @NotNull private final String name;

    public JumpInstruction(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tjmp\t" + name;
    }
}

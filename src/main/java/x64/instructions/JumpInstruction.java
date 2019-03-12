package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.pseudo.PseudoInstruction;

/** Reprsents a jump to a label, provided the condition holds true (using E_FLAGS register) */
public class JumpInstruction extends Instruction {

    @NotNull private final ConditionCode code;
    @NotNull private final String name;

    public JumpInstruction(@NotNull ConditionCode code, @NotNull String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String assemblyRepresentation() {
        return "j" + code.x64Code() + "\t" + name;
    }
}

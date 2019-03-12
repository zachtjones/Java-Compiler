package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

public class SetConditionReg extends Instruction {
    @NotNull private final ConditionCode type;
    @NotNull private final X64Register register;

    public SetConditionReg(@NotNull ConditionCode type, @NotNull X64Register register) {
        this.type = type;
        this.register = register;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tset" + type.x64Code() + " " + register;
    }
}

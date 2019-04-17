package x64.instructions;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.operands.X64Register;

/** If the condition passes, makes the register hold the value 1, otherwise 0.*/
public class SetConditionReg extends Instruction {

    /** Sets the register's value if the condition holds to 1, otherwise 0. */
    public SetConditionReg(@NotNull ConditionCode type, @NotNull X64Register register) {
		super("\tset" + type.x64Code() + " " + register.byteSizeRep());
    }
}

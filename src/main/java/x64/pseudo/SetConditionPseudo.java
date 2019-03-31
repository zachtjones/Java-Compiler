package x64.pseudo;

import helper.ConditionCode;
import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.Instruction;
import x64.instructions.SetConditionBasePointerOffset;
import x64.instructions.SetConditionReg;
import x64.operands.X64PseudoRegister;

import java.util.Collections;
import java.util.List;

public class SetConditionPseudo implements PseudoInstruction {
    @NotNull private final ConditionCode type;
    @NotNull private final X64PseudoRegister destination;

    public SetConditionPseudo(@NotNull ConditionCode type, @NotNull X64PseudoRegister destination) {
        this.type = type;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(destination, i);
    }

    @Override
    public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

        if (context.isRegister(destination)) {
            return Collections.singletonList(
                new SetConditionReg(type, context.getRegister(destination))
            );
        } else {
            return Collections.singletonList(
                new SetConditionBasePointerOffset(type, context.getBasePointer(destination))
            );
        }
    }

    @Override
    public String toString() {
        return "\tset" + type.x64Code() + " " + destination;
    }
}

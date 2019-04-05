package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.BinaryRegToRIPRelative;
import x64.instructions.Instruction;
import x64.instructions.MoveBPOffsetToReg;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BinaryPseudoToRIPRelative implements PseudoInstruction {

    @NotNull public final X64PseudoRegister source;
    @NotNull public final RIPRelativeData destination;
    @NotNull private final String name;


    BinaryPseudoToRIPRelative(@NotNull String name, @NotNull X64PseudoRegister source,
									 @NotNull RIPRelativeData destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(source, i);
    }

    @NotNull
    abstract BinaryRegToRIPRelative createThisRegToRipRelative(@NotNull X64Register source,
                                                               @NotNull RIPRelativeData destination);

    @Override
    public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
        if (context.isRegister(source)) {
            return Collections.singletonList(
                createThisRegToRipRelative(
                    context.getRegister(source),
                    destination
                )
            );
        } else {
            // need temp, can't have 2 memory operands in 1 instruction
            return Arrays.asList(
                new MoveBPOffsetToReg(
                    context.getBasePointer(source),
                    context.getScratchRegister(),
                    source.getSuffix()
                ),
                createThisRegToRipRelative(
                    context.getScratchRegister(),
                    destination
                )
            );
        }
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String toString() {
        return '\t' + name + " " +
                source.toString() + ", " + destination.toString();
    }
}

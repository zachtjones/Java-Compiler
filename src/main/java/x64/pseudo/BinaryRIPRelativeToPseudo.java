package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.BinaryRIPRelativeToReg;
import x64.instructions.Instruction;
import x64.instructions.MoveRegToBPOffset;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BinaryRIPRelativeToPseudo implements PseudoInstruction {

    @NotNull
	public final RIPRelativeData source;
    @NotNull
    public final X64PseudoRegister destination;
    private final String name;


    BinaryRIPRelativeToPseudo(String name, @NotNull RIPRelativeData source, @NotNull X64PseudoRegister destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(destination, i);
    }

    abstract @NotNull BinaryRIPRelativeToReg
        createThisRipRelativeToReg(@NotNull RIPRelativeData source, @NotNull X64Register destination);

    @Override
    public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

        if (context.isRegister(destination)) {
            return Collections.singletonList(
                createThisRipRelativeToReg(
                    source,
                    context.getRegister(destination)
                )
            );
        } else {
            // can't do direct swap, need the temporary
            return Arrays.asList(
                createThisRipRelativeToReg(
                    source,
                    context.getScratchRegister()
                ),
                new MoveRegToBPOffset(
                    context.getScratchRegister(),
                    context.getBasePointer(destination),
                    destination.getSuffix()
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

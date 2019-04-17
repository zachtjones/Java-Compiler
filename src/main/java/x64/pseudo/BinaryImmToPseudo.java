package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.allocation.RegistersUsed;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Collections;
import java.util.List;

/** Represents a binary instruction that uses an immediate source and a pseudo register destination. */
public abstract class BinaryImmToPseudo implements PseudoInstruction {

    @NotNull public final Immediate source;
    @NotNull public final X64PseudoRegister destination;
    @NotNull private final String name;


    BinaryImmToPseudo(@NotNull String name, @NotNull Immediate source,
                             @NotNull X64PseudoRegister destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(destination, i);
    }

    /** Creates the instance of the subclass that uses the immediate and register */
    abstract BinaryImmToReg createThisImmToReg(@NotNull Immediate source, @NotNull X64Register destination);

    /** Creates the instance of the subclass that uses the immediate and BP offset */
    abstract BinaryImmToBPOffset createThisImmToBPOffset(@NotNull Immediate source, @NotNull BPOffset destination);

    @Override
    public final @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
        if (context.isRegister(destination)) {
            return Collections.singletonList(
                createThisImmToReg(
                    source,
                    context.getRegister(destination)
                )
            );
        } else {
            return Collections.singletonList(
                createThisImmToBPOffset(
                    source,
                    context.getBasePointer(destination)
                )
            );
        }
    }

    @Override
    public final String toString() {
        return '\t' + name + " " +
                source.toString() + ", " + destination.toString();
    }
}

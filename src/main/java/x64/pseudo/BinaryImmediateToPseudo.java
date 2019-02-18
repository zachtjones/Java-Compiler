package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.Immediate;
import x64.operands.RIPRelativeData;
import x64.operands.X64PreservedRegister;

import java.util.Map;

/** Represents a binary instruction that uses an immediate source and a pseudo register destination. */
public abstract class BinaryImmediateToPseudo implements PseudoInstruction {

    @NotNull public final Immediate source;
    @NotNull public final X64PreservedRegister destination;
    @NotNull private final String name;


    public BinaryImmediateToPseudo(@NotNull String name, @NotNull Immediate source,
								   @NotNull X64PreservedRegister destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(destination, i);
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        mapping.get(destination).increment();
    }

    @Override
    public final String toString() {
        return '\t' + name + " " +
                source.toString() + ", " + destination.toString();
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.Immediate;

/** This class represents a binary instruction with an immediate source and base pointer offset destination */
public abstract class BinaryImmToBasePointerOffset extends Instruction {

    @NotNull private final Immediate source;
    @NotNull private final BasePointerOffset destination;
    @NotNull private final String name;


    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    public BinaryImmToBasePointerOffset(@NotNull String name, @NotNull Immediate source,
										@NotNull BasePointerOffset destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + "q " +
                source.toString() + ", " + destination.toString();
    }
}

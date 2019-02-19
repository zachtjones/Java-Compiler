package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

public abstract class BinaryRegToRegDisplacement extends Instruction {

    @NotNull private final X64Register source;
    @NotNull private final RegDisplacement destination;
    @NotNull private final String name;


    /**
     * Represents a binary operation with an register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register source.
     * @param destination The register destination.
     */
    public BinaryRegToRegDisplacement(@NotNull String name, @NotNull X64Register source,
									  @NotNull RegDisplacement destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + "q " + source.toString() + ", " + destination.toString();
    }
}

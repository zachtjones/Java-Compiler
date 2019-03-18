package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.*;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryImmToReg extends Instruction {

    @NotNull private final Immediate source;
    @NotNull private final X64Register destination;
    @NotNull private X64InstructionSize size;
    @NotNull private final String name;


    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryImmToReg(@NotNull String name, @NotNull Immediate source, @NotNull X64Register destination,
                   @NotNull X64InstructionSize size) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.size = size;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + size + " " + source + ", " + destination.assemblyRep(size);
    }
}

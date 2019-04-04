package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToBPOffset extends Instruction {

    @NotNull private final X64Register source;
    @NotNull private final BPOffset destination;
    @NotNull private final String name;
    @NotNull private final X64InstructionSize size;


    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryRegToBPOffset(@NotNull String name, @NotNull X64Register source,
                        @NotNull BPOffset destination, @NotNull X64InstructionSize size) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.size = size;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + size + " " + source.assemblyRep(size) + ", " + destination.toString();
    }
}

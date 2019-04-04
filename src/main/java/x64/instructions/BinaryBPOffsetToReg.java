package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** This class represents a binary instruction with an base pointer offset source and register destination */
public abstract class BinaryBPOffsetToReg extends Instruction {

    @NotNull private final BPOffset source;
    @NotNull private final X64Register destination;
    @NotNull private X64InstructionSize size;
    @NotNull private final String name;

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The base pointer offset source.
     * @param destination The register destination.
     */
    BinaryBPOffsetToReg(@NotNull String name, @NotNull BPOffset source,
                        @NotNull X64Register destination, @NotNull X64InstructionSize size) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.size = size;
    }

    /** Represents how this instruction should be represented */
    @Override
    public String assemblyRepresentation() {
        return '\t' + name + size + " " + source + ", " + destination.assemblyRep(size);
    }
}

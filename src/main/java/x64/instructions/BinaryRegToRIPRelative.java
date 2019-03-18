package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToRIPRelative extends Instruction {

    @NotNull private final X64Register source;
    @NotNull private final RIPRelativeData destination;
    @NotNull private X64InstructionSize size;
    @NotNull private final String name;

    BinaryRegToRIPRelative(@NotNull String name, @NotNull X64Register source,
                           @NotNull RIPRelativeData destination, @NotNull X64InstructionSize size) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.size = size;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + size + " " +
                source.toString() + ", " + destination.toString();
    }
}

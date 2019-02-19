package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToRIPRelative extends Instruction {

    @NotNull private final X64Register source;
    @NotNull private final RIPRelativeData destination;
    @NotNull private final String name;

    public BinaryRegToRIPRelative(@NotNull String name, @NotNull X64Register source,
								  @NotNull RIPRelativeData destination) {
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

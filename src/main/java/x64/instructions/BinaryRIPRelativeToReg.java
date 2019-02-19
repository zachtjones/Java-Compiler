package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRIPRelativeToReg extends Instruction {

    @NotNull private final RIPRelativeData source;
    @NotNull private final X64Register destination;
    private final String name;


    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    public BinaryRIPRelativeToReg(String name, @NotNull RIPRelativeData source,
								  @NotNull X64Register destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + destination.getSuffix() + " " +
                source.toString() + ", " + destination.toString();
    }
}

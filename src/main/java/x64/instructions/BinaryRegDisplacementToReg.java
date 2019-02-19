package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

public abstract class BinaryRegDisplacementToReg extends Instruction {

    @NotNull private final RegDisplacement source;
    @NotNull private final X64Register destination;
    @NotNull private final String name;

    public BinaryRegDisplacementToReg(@NotNull String name, @NotNull RegDisplacement source,
									  @NotNull X64Register destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + destination.getSuffix() + " " + source.toString() + ", " + destination.toString();
    }
}

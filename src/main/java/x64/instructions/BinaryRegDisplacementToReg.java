package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

public abstract class BinaryRegDisplacementToReg extends Instruction {

    @NotNull private final RegDisplacement source;
    @NotNull private final X64Register destination;
    @NotNull private X64InstructionSize size;
    @NotNull private final String name;

    BinaryRegDisplacementToReg(@NotNull String name, @NotNull RegDisplacement source,
                               @NotNull X64Register destination, @NotNull X64InstructionSize size) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.size = size;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + size + " " + source.toString() + ", " + destination.toString();
    }
}

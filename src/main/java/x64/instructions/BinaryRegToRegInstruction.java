package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToRegInstruction extends Instruction {

    @NotNull private final X64NativeRegister source;
    @NotNull private final X64NativeRegister destination;
    @NotNull private final String name;


    /**
     * Represents a binary operation with an register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register source.
     * @param destination The register destination.
     */
    public BinaryRegToRegInstruction(@NotNull String name, @NotNull X64NativeRegister source,
                                     @NotNull X64NativeRegister destination) {
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

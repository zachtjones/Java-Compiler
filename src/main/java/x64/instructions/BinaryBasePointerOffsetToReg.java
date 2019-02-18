package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;

/** This class represents a binary instruction with an base pointer offset source and register destination */
public abstract class BinaryBasePointerOffsetToReg extends Instruction {

    @NotNull private final BasePointerOffset source;
    @NotNull private final X64NativeRegister destination;
    @NotNull private final String name;

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The base pointer offset source.
     * @param destination The register destination.
     */
    public BinaryBasePointerOffsetToReg(@NotNull String name, @NotNull BasePointerOffset source,
										@NotNull X64NativeRegister destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String assemblyRepresentation() {
        return '\t' + name + destination.getSuffix() +
                source.toString() + ", " + destination.toString();
    }
}

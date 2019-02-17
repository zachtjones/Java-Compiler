package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.instructions.Instruction;
import x64.operands.*;

import java.util.Map;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryImmRegInstruction extends Instruction {

    @NotNull private final Immediate source;
    @NotNull private final X64NativeRegister destination;
    private final String name;


    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    public BinaryImmRegInstruction(String name, @NotNull Immediate source, @NotNull X64NativeRegister destination) {
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

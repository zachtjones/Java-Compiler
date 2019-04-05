package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.*;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryImmToReg extends Instruction {

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryImmToReg(@NotNull String name, @NotNull Immediate source, @NotNull X64Register destination,
                   @NotNull X64InstructionSize size) {

		super('\t' + name + size + " " + source + ", " + destination.assemblyRep(size));
    }
}

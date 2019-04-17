package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToBPOffset extends Instruction {

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryRegToBPOffset(@NotNull String name, @NotNull X64Register source,
                        @NotNull BPOffset destination, @NotNull X64InstructionSize size) {

		super('\t' + name + size + " " + source.assemblyRep(size) + ", " + destination.toString());
    }
}

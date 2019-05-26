package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.Immediate;

/** This class represents a binary instruction with an immediate source and base pointer offset destination */
public abstract class BinaryImmToBPOffset extends Instruction {

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryImmToBPOffset(@NotNull String name, @NotNull Immediate source,
                        @NotNull BPOffset destination, @NotNull X64InstructionSize size) {

		super('\t' + name + size.size + " " + source.toString() + ", " + destination.toString());
    }
}

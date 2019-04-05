package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

public abstract class BinaryRegToRegDisplacement extends Instruction {

    /**
     * Represents a binary operation with an register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register source.
     * @param destination The register destination.
     */
    BinaryRegToRegDisplacement(@NotNull String name, @NotNull X64Register source,
									  @NotNull RegDisplacement destination) {
		super('\t' + name + "q " + source.toString() + ", " + destination.toString());
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToReg extends Instruction {

    /**
     * Represents a binary operation with an register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register source.
     * @param destination The register destination.
     */
    BinaryRegToReg(@NotNull String name, @NotNull X64Register source,
                          @NotNull X64Register destination, @NotNull X64InstructionSize... size) {

		super('\t' + name + X64InstructionSize.concat(size) + " " + source.assemblyRep(size[0]) +
			", " + destination.assemblyRep(size[size.length -1])
		);
    }
}

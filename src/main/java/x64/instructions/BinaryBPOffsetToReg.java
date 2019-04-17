package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** This class represents a binary instruction with an base pointer offset source and register destination */
public abstract class BinaryBPOffsetToReg extends Instruction {

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The base pointer offset source.
     * @param destination The register destination.
     */
    BinaryBPOffsetToReg(@NotNull String name, @NotNull BPOffset source,
                        @NotNull X64Register destination, @NotNull X64InstructionSize... sizes) {

		super('\t' + name + X64InstructionSize.concat(sizes) + " " + source + ", " +
			destination.assemblyRep(sizes[sizes.length - 1])
		);
    }
}

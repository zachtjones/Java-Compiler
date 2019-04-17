package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegAbsolute;
import x64.operands.X64Register;

public abstract class BinaryRegToRegAbsolute extends Instruction {

    /**
     * Represents a binary operation with an register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register source.
     * @param destination The register destination that is a pointer.
	 * @param size The operand size to use.
     */
    BinaryRegToRegAbsolute(@NotNull String name, @NotNull X64Register source,
						   @NotNull RegAbsolute destination, @NotNull X64InstructionSize size) {
		super('\t' + name + size + " " + source.toString() + ", " + destination.toString());
    }
}

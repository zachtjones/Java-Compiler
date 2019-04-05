package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegDisplacement;
import x64.operands.X64Register;

public abstract class BinaryRegDisplacementToReg extends Instruction {

    /**
     * Represents a binary operation offset(register), register
     * @param name The name of the operation.
     * @param source The offset(register) used.
     * @param destination The register that gets updated.
     * @param size The size of the memory to operate on.
     */
    BinaryRegDisplacementToReg(@NotNull String name, @NotNull RegDisplacement source,
                               @NotNull X64Register destination, @NotNull X64InstructionSize size) {

		super('\t' + name + size + " " + source.toString() + ", " + destination.toString());
    }
}

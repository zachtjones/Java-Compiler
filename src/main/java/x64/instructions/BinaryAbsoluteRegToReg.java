package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegAbsolute;
import x64.operands.X64Register;

/** This class represents a binary instruction with a memory at register source and register destination */
public abstract class BinaryAbsoluteRegToReg extends Instruction {

    /**
     * Represents a binary operation with a memory at register source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The register absolute address source.
     * @param destination The register destination.
     */
    BinaryAbsoluteRegToReg(@NotNull String name, @NotNull RegAbsolute source,
                           @NotNull X64Register destination, @NotNull X64InstructionSize size) {

        super('\t' + name + size + " " + source.toString() + ", " + destination.toString());
    }
}

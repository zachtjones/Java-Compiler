package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRIPRelativeToReg extends Instruction {

    /**
     * Represents a binary operation with an immediate source and register destination.
     * @param name The binary instruction's name, like 'add'
     * @param source The Immediate source.
     * @param destination The register destination.
     */
    BinaryRIPRelativeToReg(@NotNull String name, @NotNull RIPRelativeData source,
                                  @NotNull X64Register destination, @NotNull X64InstructionSize size) {
		super('\t' + name + size + " " + source.toString() + ", " + destination.toString());
    }
}

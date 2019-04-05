package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

/** This class represents a binary instruction with an immediate source and register destination */
public abstract class BinaryRegToRIPRelative extends Instruction {

    /**
     * Represents a binary operation on a register source and instruction pointer offset.
     * @param name The name of the instruction.
     * @param source The source, a register
     * @param destination The destination, an offset from the instruction pointer.
     * @param size The size of memory to operate on.
     */
    BinaryRegToRIPRelative(@NotNull String name, @NotNull X64Register source,
                           @NotNull RIPRelativeData destination, @NotNull X64InstructionSize size) {

		super('\t' + name + size + " " + source.toString() + ", " + destination.toString());
    }
}

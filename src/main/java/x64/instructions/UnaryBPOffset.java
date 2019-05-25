package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.BPOffset;
import x64.operands.X64Register;

/** This class represents a unary instruction with a base pointer source */
public abstract class UnaryBPOffset extends Instruction {

    /**
     * Represents a unary instruction with a base pointer source.
     * @param name The unary instruction's name, like 'not'
     * @param operand The Immediate source.
     * @param size The size of the memory to use.
     */
    UnaryBPOffset(@NotNull String name, @NotNull BPOffset operand, @NotNull X64InstructionSize size) {

        super('\t' + name + size + " " + operand.toString());
    }
}
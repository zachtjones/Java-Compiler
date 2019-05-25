package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** This class represents a unary instruction with a register source */
public abstract class UnaryReg extends Instruction {

    /**
     * Represents a unary instruction with a register source.
     * @param name The unary instruction's name, like 'not'
     * @param operand The Immediate source.
     * @param size The size of the register.
     */
    UnaryReg(@NotNull String name, @NotNull X64Register operand, @NotNull X64InstructionSize size) {

        super('\t' + name + size + " " + operand.assemblyRep(size));
    }
}
package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** Represents the 2's complement of a register (0 - the operand) */
public class NegationReg extends UnaryReg {
    public NegationReg(@NotNull X64Register operand, X64InstructionSize suffix) {
        super("neg", operand, suffix);
    }
}

package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.X64Register;

/** Represents a not operation on a hardware register */
public class NotReg extends UnaryReg {
    public NotReg(@NotNull X64Register operand, X64InstructionSize suffix) {
        super("not", operand, suffix);
    }
}

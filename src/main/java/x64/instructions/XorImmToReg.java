package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.Immediate;
import x64.operands.X64Register;

/** Represents an xor of an immediate and a register. */
public class XorImmToReg extends BinaryImmToReg {
    public XorImmToReg(@NotNull Immediate source, @NotNull X64Register destination, X64InstructionSize suffix) {
        super("xor", source, destination, suffix);
    }
}

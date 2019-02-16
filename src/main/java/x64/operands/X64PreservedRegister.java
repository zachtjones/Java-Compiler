package x64.operands;

import org.jetbrains.annotations.NotNull;
import x64.Instruction;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

/** This is an abstraction of a hardware register that is preserved across function calls */
public class X64PreservedRegister implements Operand {

    private final int number;
    private final Instruction.Size size;
    public X64PreservedRegister(int number, @NotNull Instruction.Size size) {
        this.number = number;
        this.size = size;
    }

    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public int hashCode() {
        return number ^ size.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof X64PreservedRegister &&
            ("%" + ((X64PreservedRegister) other).size.size + ((X64PreservedRegister) other).number).equals("%" + size.size + number);
    }

    @Override
    public String toString() {
        return "%" + size.size + number;
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        usedRegs.markUsed(this, i);
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        mapping.get(this).increment();
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        usedRegs.markDefined(this, i);
    }
}

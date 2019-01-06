package x64.operands;

import x64.Instruction;

/** This is an abstraction of a hardware register that is preserved across function calls */
public class X64PreservedRegister {

    private final int number;
    private final Instruction.Size size;
    public X64PreservedRegister(int number, Instruction.Size size) {
        this.number = number;
        this.size = size;
    }

    Instruction.Size getSuffix() {
        return size;
    }

    public String assemblyRep() {
        return "%" + size.size + number;
    }

    @Override
    public int hashCode() {
        return number ^ size.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof X64PreservedRegister &&
            ((X64PreservedRegister)other).assemblyRep().equals(this.assemblyRep());
    }
}

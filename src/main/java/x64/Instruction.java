package x64;

import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

public interface Instruction {

    enum Size {
        // integer ones, quad can also be pointer
        BYTE('b'), WORD('w'), LONG('l'), QUAD('q'),

        // floating-point numbers
        SINGLE('s'), DOUBLE('d');

        public char size;
        Size(char suffix) {
            this.size = suffix;
        }
    }

    /** Returns true if and only if this instruction is a variety of call */
    default boolean isCalling() {
        return false;
    }

    /** Call the methods for the marking the registers as used / defined. */
    default void markRegisters(int i, RegistersUsed usedRegs) {}

    /** Swaps out the X64Preserved registers to their allocated one */
    default void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {}

    /** Represents how this instruction should be represented in x64 assembly */
    String toString();
}

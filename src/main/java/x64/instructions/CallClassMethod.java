package x64.instructions;

import x64.Instruction;
import x64.SymbolNames;
import x64.allocation.RegistersUsed;
import x64.allocation.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

/**
 * Represents a call to a routine specified by the class/method names
 */
public class CallClassMethod implements Instruction {

    private final String label;

    public CallClassMethod(String className, String name) {
        label = SymbolNames.getMethodName(className, name);
    }

    @Override
    public boolean isCalling() {
        return true;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        // doesn't use registers
    }

    @Override
    public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        // nothing to do, doesn't use registers
    }

    @Override
    public String toString() {
        return "\tcall " + label;
    }
}

package x64.instructions;

import x64.Instruction;
import x64.SymbolNames;

/**
 * Represents a call to a routine specified by the class/method names.
 * These calls should be put in the procedure linkage table
 */
public class CallClassMethod implements Instruction {

    private final String label;

    public CallClassMethod(String className, String name) {
        label = SymbolNames.getMethodName(className, name) + "@PLT";
    }

    public CallClassMethod(String label) {
        this.label = label + "@PLT";
    }

    @Override
    public boolean isCalling() {
        return true;
    }

    @Override
    public String toString() {
        return "\tcall " + label;
    }
}

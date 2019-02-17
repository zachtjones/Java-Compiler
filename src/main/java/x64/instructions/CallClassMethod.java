package x64.instructions;

import x64.pseudoInstruction.PseudoInstruction;
import x64.SymbolNames;

import static x64.allocation.CallingConvention.isLinux;

/**
 * Represents a call to a routine specified by the class/method names.
 * These calls should be put in the procedure linkage table
 */
public class CallClassMethod implements PseudoInstruction {

    private final String label;

    public CallClassMethod(String className, String name) {
        label = SymbolNames.getMethodName(className, name) + (isLinux ? "@PLT" : "");
    }

    public CallClassMethod(String label) {
        this.label = label + (isLinux ? "@PLT": "");
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

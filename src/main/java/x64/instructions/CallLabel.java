package x64.instructions;

import x64.SymbolNames;

import static x64.allocation.CallingConvention.isLinux;

/**
 * Represents a call to a routine specified by the class/method names.
 * These calls should be put in the procedure linkage table
 */
public class CallLabel extends Instruction {

    private final String label;

    public CallLabel(String className, String functionName) {
        label = SymbolNames.getMethodName(className, functionName) + (isLinux ? "@PLT" : "");
    }

    public CallLabel(String label) {
        this.label = label + (isLinux ? "@PLT": "");
    }

    @Override
    public boolean isCalling() {
        return true;
    }

    @Override
    public String assemblyRepresentation() {
        return "\tcall " + label;
    }
}

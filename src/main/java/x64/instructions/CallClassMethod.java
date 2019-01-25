package x64.instructions;

import x64.Instruction;
import x64.SymbolNames;

/**
 * Represents a call to a routine specified by the class/method names
 */
public class CallClassMethod implements Instruction {

    private final String label;

    public CallClassMethod(String className, String name) {
        label = SymbolNames.getMethodName(className, name);
    }

    public CallClassMethod(String label) {
        this.label = label;
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

package x64.instructions;

import x64.SymbolNames;

import static x64.allocation.CallingConvention.isLinux;

/**
 * Represents a call to a routine specified by the class/method names.
 * These calls should be put in the procedure linkage table
 */
public class CallLabel extends Instruction {

    public CallLabel(String className, String functionName) {
		super("\tcall " + SymbolNames.getMethodName(className, functionName) + suffix());
    }

    public CallLabel(String label) {
		super("\tcall " + label + suffix());
    }

    /** Helper method for the suffix required on labels (@PLT on linux systems.) */
    private static String suffix() {
		return isLinux ? "@PLT" : "";
	}

    @Override
    public boolean isCalling() {
        return true;
    }
}

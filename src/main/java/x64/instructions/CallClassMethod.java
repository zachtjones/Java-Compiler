package x64.instructions;

import x64.Instruction;
import x64.SymbolNames;

public class CallClassMethod implements Instruction {

    public CallClassMethod(String className, String name) {
        final String label = SymbolNames.getMethodName(className, name);

    }
}

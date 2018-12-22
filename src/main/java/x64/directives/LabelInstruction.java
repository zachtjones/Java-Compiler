package x64.directives;

import x64.Instruction;

public class LabelInstruction implements Instruction {
    private final String name;

    public LabelInstruction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":";
    }
}

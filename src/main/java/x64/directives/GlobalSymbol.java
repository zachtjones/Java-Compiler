package x64.directives;

import x64.Instruction;

/** Represents the .global NAME directive, useful for imports and exports. */
public class GlobalSymbol implements Instruction {
    private final String name;

    public GlobalSymbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ".global " + name;
    }
}

package x64.operands;

import x64.SymbolNames;

/** Represents displacement from the instruction pointer, OFFSET(%rip) in x64 */
public class RIPRelativeData {

    private final String label;

    private RIPRelativeData(String label) {
        this.label = label;
    }

    /** Convenience method for creating a PC relative data from the label */
    public static RIPRelativeData pointerFromLabel(String label) {
        return new RIPRelativeData(label);
    }

    /** Convenience method for creating a reference to a field in a class, using the Register as the size. */
    public static RIPRelativeData fromField(String className, String fieldName) {
        final String symbolName = SymbolNames.getFieldName(className, fieldName);
        return new RIPRelativeData(symbolName);
    }

    @Override
    public String toString() {
        return label + "(%rip)";
    }
}

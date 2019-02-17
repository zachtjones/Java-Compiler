package x64.operands;

import x64.SymbolNames;

/** Represents displacement from the program counter, OFFSET(%rip) in x64 */
public class PCRelativeData {

    private final String label;

    private PCRelativeData(String label) {
        this.label = label;
    }

    /** Convenience method for creating a PC relative data from the label */
    public static PCRelativeData pointerFromLabel(String label) {
        return new PCRelativeData(label);
    }

    /** Convenience method for creating a reference to a field in a class, using the Register as the size. */
    public static PCRelativeData fromField(String className, String fieldName) {
        final String symbolName = SymbolNames.getFieldName(className, fieldName);
        return new PCRelativeData(symbolName);
    }

    @Override
    public String toString() {
        return label + "(%rip)";
    }
}

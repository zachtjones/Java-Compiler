package x64;

import intermediate.Register;

public class PCRelativeData implements SourceOperand, DestinationOperand {

    private final String label;
    private final Instruction.Size size;

    private PCRelativeData(String label, Instruction.Size size) {
        this.label = label;
        this.size = size;
    }

    /** Convenience method for creating a reference to a field in a class, using the Register as the size. */
    public static SourceOperand fromField(String className, String fieldName, Register result) {
        final String symbolName = SymbolNames.getFieldName(className, fieldName);
        final Instruction.Size size = result.x64Type();
        return new PCRelativeData(symbolName, size);
    }

    @Override
    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public String assemblyRep() {
        return label + "(%rip)";
    }
}

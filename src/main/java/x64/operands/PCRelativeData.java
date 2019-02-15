package x64.operands;

import intermediate.Register;
import x64.Instruction;
import x64.SymbolNames;
import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;

import java.util.Map;

/** Represents displacement from the program counter, OFFSET(%rip) in x64 */
public class PCRelativeData implements SourceOperand, DestinationOperand {

    private final String label;
    private final Instruction.Size size;

    private PCRelativeData(String label, Instruction.Size size) {
        this.label = label;
        this.size = size;
    }

    /** Convenience method for creating a PC relative data from the label */
    public static PCRelativeData pointerFromLabel(String label) {
        return new PCRelativeData(label, Instruction.Size.QUAD);
    }

    /** Convenience method for creating a reference to a field in a class, using the Register as the size. */
    public static PCRelativeData fromField(String className, String fieldName, Register result) {
        final String symbolName = SymbolNames.getFieldName(className, fieldName);
        final Instruction.Size size = result.x64Type();
        return new PCRelativeData(symbolName, size);
    }

    @Override
    public Instruction.Size getSuffix() {
        return size;
    }

    @Override
    public String toString() {
        return label + "(%rip)";
    }

    @Override
    public void markUsed(int i, RegistersUsed usedRegs) {
        // uses the instruction pointer register, no preserved registers
    }

    @Override
    public void swapOut(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        // same as markUsed
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        // uses RIP, not preserved registers
    }

    @Override
    public void markDefined(int i, RegistersUsed usedRegs) {
        // same as markUsed
    }
}

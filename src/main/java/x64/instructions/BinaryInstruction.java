package x64.instructions;

import x64.allocation.RegisterMapped;
import x64.allocation.RegistersUsed;
import x64.operands.DestinationOperand;
import x64.pseudoInstruction.PseudoInstruction;
import x64.operands.SourceOperand;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

public abstract class BinaryInstruction implements PseudoInstruction {

    private final SourceOperand source;
    private final DestinationOperand destination;
    private final String name;


    BinaryInstruction(String name, SourceOperand source, DestinationOperand destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        source.markUsed(i, usedRegs);
        destination.markDefined(i, usedRegs);
    }

    @Override
    public void prioritizeRegisters(Map<X64PreservedRegister, RegisterMapped> mapping) {
        source.prioritizeRegisters(mapping);
        destination.prioritizeRegisters(mapping);
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String toString() {
        return '\t' + name + destination.getSuffix() + " " +
                source.toString() + ", " + destination.toString();
    }
}

package x64.instructions;

import x64.allocation.RegistersUsed;
import x64.operands.DestinationOperand;
import x64.Instruction;
import x64.operands.SourceOperand;

public abstract class BinaryInstruction implements Instruction {

    private final SourceOperand source;
    private final DestinationOperand destination;
    private final String name;


    BinaryInstruction(String name, SourceOperand source, DestinationOperand destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean isCalling() {
        return false;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        source.markUsed(i, usedRegs);
        destination.markDefined(i, usedRegs);
    }

    /** Represents how this instruction should be represented */
    @Override
    public final String toString() {
        return '\t' + name + destination.getSuffix().size + " " +
                source.assemblyRep() + ", " + destination.assemblyRep();
    }
}

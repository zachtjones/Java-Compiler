package x64.instructions;

import x64.DestinationOperand;
import x64.Instruction;
import x64.SourceOperand;

public abstract class BinaryInstruction implements Instruction {

    private final SourceOperand source;
    private final DestinationOperand destination;
    private final String name;


    public BinaryInstruction(String name, SourceOperand source, DestinationOperand destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    /** Represents how this instruction should be represented */
    public final String toString() {
        return name + destination.getSuffix().size + " " + source.assemblyRep() + ", " + destination.assemblyRep();
    }
}

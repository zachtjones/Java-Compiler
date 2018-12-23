package x64.instructions;

import x64.DestinationOperand;
import x64.SourceOperand;

public class MoveInstruction extends BinaryInstruction {

    public MoveInstruction(SourceOperand source, DestinationOperand destination) {
        super("mov", source, destination);
    }
}

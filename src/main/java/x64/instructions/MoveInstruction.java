package x64.instructions;

import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;

public class MoveInstruction extends BinaryInstruction {

    public MoveInstruction(SourceOperand source, DestinationOperand destination) {
        super("mov", source, destination);
    }
}

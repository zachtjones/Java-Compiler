package x64.instructions;

import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;

public class LoadEffectiveAddressInstruction extends BinaryInstruction {

    public LoadEffectiveAddressInstruction(SourceOperand source, DestinationOperand destination) {
        super("lea", source, destination);
    }
}

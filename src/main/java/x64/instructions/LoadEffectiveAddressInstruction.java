package x64.instructions;

import x64.DestinationOperand;
import x64.SourceOperand;

public class LoadEffectiveAddressInstruction extends BinaryInstruction {

    public LoadEffectiveAddressInstruction(SourceOperand source, DestinationOperand destination) {
        super("lea", source, destination);
    }
}

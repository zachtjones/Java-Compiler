package x64.instructions;

import x64.pseudo.BinaryInstruction;

public class LoadEffectiveAddressInstruction extends BinaryInstruction {

    public LoadEffectiveAddressInstruction(SourceOperand source, DestinationOperand destination) {
        super("lea", source, destination);
    }
}

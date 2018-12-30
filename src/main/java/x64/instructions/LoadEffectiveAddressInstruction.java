package x64.instructions;

import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

public class LoadEffectiveAddressInstruction extends BinaryInstruction {

    public LoadEffectiveAddressInstruction(SourceOperand source, DestinationOperand destination) {
        super("lea", source, destination);
    }
}

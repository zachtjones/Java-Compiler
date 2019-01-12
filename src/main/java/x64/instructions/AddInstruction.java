package x64.instructions;

import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;

public class AddInstruction extends BinaryInstruction {
	public AddInstruction(SourceOperand source, DestinationOperand destination) {
		super("add", source, destination);
	}
}

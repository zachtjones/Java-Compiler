package x64.instructions;

import x64.operands.DestinationOperand;
import x64.operands.SourceOperand;

public class SubtractInstruction extends BinaryInstruction {
	public SubtractInstruction(SourceOperand source, DestinationOperand destination) {
		super("sub", source, destination);
	}
}

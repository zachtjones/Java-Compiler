package x64.instructions;

import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class AddRegToBasePointerOffset extends BinaryRegToBasePointerOffset {
	public AddRegToBasePointerOffset(X64Register source, BasePointerOffset destination) {
		super("add", source, destination);
	}
}

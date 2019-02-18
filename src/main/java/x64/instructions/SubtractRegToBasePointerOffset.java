package x64.instructions;

import x64.operands.BasePointerOffset;
import x64.operands.X64Register;

public class SubtractRegToBasePointerOffset extends BinaryRegToBasePointerOffset {
	public SubtractRegToBasePointerOffset(X64Register source, BasePointerOffset destination) {
		super("sub", source, destination);
	}
}

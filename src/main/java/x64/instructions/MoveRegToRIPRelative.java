package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.RIPRelativeData;
import x64.operands.X64Register;

public class MoveRegToRIPRelative extends BinaryRegToRIPRelative {

	public MoveRegToRIPRelative(@NotNull X64Register source, @NotNull RIPRelativeData destination) {
		super("mov", source, destination);
	}
}

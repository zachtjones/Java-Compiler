package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRegToRIPRelative;
import x64.instructions.MoveRegToRIPRelative;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MovePseudoToRIPRelative extends BinaryPseudoToRIPRelative {

	public MovePseudoToRIPRelative(@NotNull X64PseudoRegister source,
								   @NotNull RIPRelativeData destination) {
		super("mov", source, destination);
	}


	@Override
	@NotNull BinaryRegToRIPRelative createThisRegToRipRelative(@NotNull X64Register source,
															   @NotNull RIPRelativeData destination) {
		return new MoveRegToRIPRelative(source, destination, this.source.getSuffix());
	}
}

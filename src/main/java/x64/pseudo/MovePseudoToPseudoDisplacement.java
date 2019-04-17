package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRegToRegDisplacement;
import x64.instructions.MoveRegToRegDisplacement;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MovePseudoToPseudoDisplacement extends BinaryPseudoToPseudoDisplacement {

	public MovePseudoToPseudoDisplacement(@NotNull X64PseudoRegister source,
										  @NotNull PseudoDisplacement destination) {

		super("mov", source, destination);
	}

	@Override
	BinaryRegToRegDisplacement createThisRegToRegDisplacement(@NotNull X64Register source,
															  @NotNull RegDisplacement destination) {
		return new MoveRegToRegDisplacement(source, destination, this.source.getSuffix());
	}
}

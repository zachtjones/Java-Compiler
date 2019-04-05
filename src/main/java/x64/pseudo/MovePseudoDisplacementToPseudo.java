package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRegDisplacementToReg;
import x64.instructions.MoveRegDisplacementToReg;
import x64.operands.PseudoDisplacement;
import x64.operands.RegDisplacement;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MovePseudoDisplacementToPseudo extends BinaryPseudoDisplacementToPseudo {
	public MovePseudoDisplacementToPseudo(@NotNull PseudoDisplacement source,
										  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	BinaryRegDisplacementToReg createThisRegDisplacementToReg(@NotNull RegDisplacement source, @NotNull X64Register destination) {
		return new MoveRegDisplacementToReg(source, destination, this.destination.getSuffix());
	}
}

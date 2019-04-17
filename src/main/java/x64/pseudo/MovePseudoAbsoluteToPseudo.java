package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryAbsoluteRegToReg;
import x64.instructions.MoveRegAbsoluteToReg;
import x64.operands.PseudoAbsolute;
import x64.operands.RegAbsolute;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MovePseudoAbsoluteToPseudo extends BinaryPseudoAbsoluteToPseudo {

	public MovePseudoAbsoluteToPseudo(@NotNull PseudoAbsolute source,
									  @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	BinaryAbsoluteRegToReg createThisRegAbsoluteToReg(@NotNull RegAbsolute source, @NotNull X64Register destination) {
		return new MoveRegAbsoluteToReg(source, destination, this.destination.getSuffix());
	}
}

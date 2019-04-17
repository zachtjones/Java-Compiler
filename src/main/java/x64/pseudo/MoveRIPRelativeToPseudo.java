package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRIPRelativeToReg;
import x64.instructions.MoveRIPRelativeToReg;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MoveRIPRelativeToPseudo extends BinaryRIPRelativeToPseudo {

	public MoveRIPRelativeToPseudo(@NotNull RIPRelativeData source,
								   @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	@NotNull BinaryRIPRelativeToReg createThisRipRelativeToReg(@NotNull RIPRelativeData source,
															   @NotNull X64Register destination) {
		return new MoveRIPRelativeToReg(source, destination, this.destination.getSuffix());
	}
}

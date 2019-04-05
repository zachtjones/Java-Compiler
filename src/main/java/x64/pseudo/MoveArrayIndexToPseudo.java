package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryArrayIndexToReg;
import x64.instructions.MoveArrayIndexToReg;
import x64.operands.PseudoIndexing;
import x64.operands.RegIndexing;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MoveArrayIndexToPseudo extends BinaryArrayIndexToPseudo {

	public MoveArrayIndexToPseudo(@NotNull PseudoIndexing source, @NotNull X64PseudoRegister destination) {
		super(source, destination);
	}

	@Override
	BinaryArrayIndexToReg createThisArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination) {
		return new MoveArrayIndexToReg(source, destination, this.destination.getSuffix());
	}
}

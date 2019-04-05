package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryArrayIndexToReg;
import x64.instructions.BinaryRegToBPOffset;
import x64.instructions.MoveArrayIndexToReg;
import x64.instructions.MoveRegToBPOffset;
import x64.operands.*;

public class MoveArrayIndexToPseudo extends BinaryArrayIndexToPseudo {

	public MoveArrayIndexToPseudo(@NotNull PseudoIndexing source, @NotNull X64PseudoRegister destination) {
		super(source, destination);
	}

	@Override
	BinaryArrayIndexToReg createThisArrayIndexToReg(@NotNull RegIndexing source, @NotNull X64Register destination) {
		return new MoveArrayIndexToReg(source, destination, this.destination.getSuffix());
	}

	@Override
	BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
		return new MoveRegToBPOffset(source, destination, this.destination.getSuffix());
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryImmToBPOffset;
import x64.instructions.BinaryImmToReg;
import x64.instructions.MoveImmToBPOffset;
import x64.instructions.MoveImmToReg;
import x64.operands.BPOffset;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MoveImmToPseudo extends BinaryImmToPseudo {

	public MoveImmToPseudo(@NotNull Immediate source, @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	BinaryImmToReg createThisImmToReg(@NotNull Immediate source, @NotNull X64Register destination) {
		return new MoveImmToReg(source, destination, this.destination.getSuffix());
	}

	@Override
	BinaryImmToBPOffset createThisImmToBPOffset(@NotNull Immediate source, @NotNull BPOffset destination) {
		return new MoveImmToBPOffset(source, destination, this.destination.getSuffix());
	}
}

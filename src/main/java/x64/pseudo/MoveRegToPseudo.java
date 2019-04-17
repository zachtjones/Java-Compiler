package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryRegToBPOffset;
import x64.instructions.BinaryRegToReg;
import x64.instructions.MoveRegToBPOffset;
import x64.instructions.MoveRegToReg;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MoveRegToPseudo extends BinaryRegToPseudoReg {

	public MoveRegToPseudo(@NotNull X64Register source,
						   @NotNull X64PseudoRegister destination) {
		super("mov", source, destination);
	}

	@Override
	@NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		return new MoveRegToReg(source, destination, this.destination.getSuffix());
	}

	@Override
	@NotNull BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
		return new MoveRegToBPOffset(source, destination, this.destination.getSuffix());
	}
}

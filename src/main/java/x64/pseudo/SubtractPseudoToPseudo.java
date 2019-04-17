package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class SubtractPseudoToPseudo extends BinaryPseudoToPseudo {
	public SubtractPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	@NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		return new SubtractRegToReg(source, destination, this.source.getSuffix());
	}

	@Override
	@NotNull BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
		return new SubtractRegToBPOffset(source, destination, this.source.getSuffix());
	}

	@Override
	@NotNull BinaryBPOffsetToReg createThisBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination) {
		return new SubtractBPOffsetToReg(source, destination, this.source.getSuffix());
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.*;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class AddPseudoToPseudo extends BinaryPseudoToPseudo {
	public AddPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	@NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		return new AddRegToReg(source, destination, this.source.getSuffix());
	}

	@NotNull
	@Override
	BinaryRegToBPOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BPOffset destination) {
		return new AddRegToBPOffset(source, destination, this.source.getSuffix());
	}

	@NotNull
	@Override
	BinaryBPOffsetToReg createThisBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination) {
		return new AddBPOffsetToReg(source, destination, this.source.getSuffix());
	}
}

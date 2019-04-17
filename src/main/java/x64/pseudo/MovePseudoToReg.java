package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.BinaryBPOffsetToReg;
import x64.instructions.BinaryRegToReg;
import x64.instructions.MoveBPOffsetToReg;
import x64.instructions.MoveRegToReg;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class MovePseudoToReg extends BinaryPseudoToReg {

	public MovePseudoToReg(@NotNull X64PseudoRegister source,
						   @NotNull X64Register destination) {
		super("mov", source, destination);
	}

	@Override
	@NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination) {
		return new MoveRegToReg(source, destination, this.source.getSuffix());
	}

	@Override
	@NotNull BinaryBPOffsetToReg createThisBPOffsetToReg(@NotNull BPOffset source, @NotNull X64Register destination) {
		return new MoveBPOffsetToReg(source, destination, this.source.getSuffix());
	}

}

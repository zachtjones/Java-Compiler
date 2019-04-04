package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.instructions.*;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

public class SubtractPseudoToPseudo extends BinaryPseudoToPseudo {
	public SubtractPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		super("add", source, destination);
	}

	@Override
	@NotNull BinaryRegToReg createThisRegToReg(@NotNull X64Register source, @NotNull X64Register destination, @NotNull X64InstructionSize size) {
		return new SubtractRegToReg(source, destination, size);
	}

	@Override
	@NotNull BinaryRegToBasePointerOffset createThisRegToBPOffset(@NotNull X64Register source, @NotNull BasePointerOffset destination, @NotNull X64InstructionSize size) {
		return new SubtractRegToBasePointerOffset(source, destination, size);
	}

	@Override
	@NotNull BinaryBasePointerOffsetToReg createThisBPOffsetToReg(@NotNull BasePointerOffset source, @NotNull X64Register destination, @NotNull X64InstructionSize size) {
		return new SubtractBasePointerOffsetToReg(source, destination, size);
	}
}

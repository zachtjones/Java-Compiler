package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.instructions.Instruction;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.ZeroExtendBasePointerOffsetToReg;
import x64.instructions.ZeroExtendRegToReg;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ZeroExtendPseudoToPseudo extends BinaryPseudoToPseudo {

	public ZeroExtendPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		// move with zero extension
		super("movz", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {

		if (context.isRegister(source)) {
			if (context.isRegister(destination)) {
				// simple move source to destination
				return Collections.singletonList(
					new ZeroExtendRegToReg(
						context.getRegister(source),
						context.getRegister(destination),
						source.getSuffix(),
						destination.getSuffix()
					)
				);
			} else {
				// there isn't a sign extend to memory, only source can be memory
				return Arrays.asList(
					new ZeroExtendRegToReg(
						context.getRegister(source),
						context.getScratchRegister(),
						source.getSuffix(),
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (context.isRegister(destination)) {
				// can be done with one instruction too
				return Collections.singletonList(
					new ZeroExtendBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getRegister(destination),
						source.getSuffix(),
						destination.getSuffix()
					)
				);
			} else {
				// both of these are base pointer offsets, but can only have 1 mem operand
				// the only complicated one, one way is to move to the temp immediate and then sign extend
				// also only source can be memory, so have to do this:
				// 1. local source zero extend to temporary immediate
				// 2. move temporary immediate to base pointer destination
				return Arrays.asList(
					new ZeroExtendBasePointerOffsetToReg(
						context.getBasePointer(source),
						context.getScratchRegister(),
						source.getSuffix(),
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						context.getScratchRegister(),
						context.getBasePointer(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

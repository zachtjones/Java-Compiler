package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.ZeroExtendBasePointerOffsetToReg;
import x64.instructions.ZeroExtendRegToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ZeroExtendPseudoToPseudo extends BinaryPseudoToPseudo {

	public ZeroExtendPseudoToPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister destination) {
		// move with zero extension
		super("movz", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {

		if (mapping.containsKey(source)) {
			if (mapping.containsKey(destination)) {
				// simple move source to destination
				return Collections.singletonList(
					new ZeroExtendRegToReg(
						mapping.get(source),
						mapping.get(destination),
						source.getSuffix(),
						destination.getSuffix()
					)
				);
			} else {
				// there isn't a sign extend to memory, only source can be memory
				return Arrays.asList(
					new ZeroExtendRegToReg(
						mapping.get(source),
						temporaryImmediate,
						source.getSuffix(),
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						temporaryImmediate,
						locals.get(destination),
						destination.getSuffix()
					)
				);
			}
		} else {
			if (mapping.containsKey(destination)) {
				// can be done with one instruction too
				return Collections.singletonList(
					new ZeroExtendBasePointerOffsetToReg(
						locals.get(source),
						mapping.get(destination),
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
						locals.get(source),
						temporaryImmediate,
						source.getSuffix(),
						destination.getSuffix()
					),
					new MoveRegToBasePointerOffset(
						temporaryImmediate,
						locals.get(destination),
						destination.getSuffix()
					)
				);
			}
		}
	}
}

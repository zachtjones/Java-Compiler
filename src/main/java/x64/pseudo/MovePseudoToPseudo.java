package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.instructions.MoveRegToRegInstruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoToPseudo extends BinaryPseudoToPseudo {

	public MovePseudoToPseudo(@NotNull X64PreservedRegister source,
							  @NotNull X64PreservedRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {

		if (locals.containsKey(source)) {
			if (locals.containsKey(destination)) {
				// both are real registers
				return Collections.singletonList(
					new MoveRegToRegInstruction(mapping.get(source), mapping.get(destination))
				);
			} else {
				// destination is a local
				return Collections.singletonList(
					new MoveRegToBasePointerOffset(mapping.get(source), locals.get(destination))
				);
			}
		} else {
			if (locals.containsKey(destination)) {
				// source -- base pointer, destination, real
				return Collections.singletonList(
					new MoveBasePointerOffsetToReg(locals.get(source), mapping.get(destination))
				);
			} else {
				// both are base pointer offset, need to use the temporary one
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(locals.get(source), temporaryImmediate),
					new MoveRegToBasePointerOffset(temporaryImmediate, locals.get(destination))
				);
			}
		}
	}
}

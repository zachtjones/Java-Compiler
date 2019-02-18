package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegAbsoluteToReg;
import x64.instructions.MoveRegToBasePointerOffset;
import x64.operands.BasePointerOffset;
import x64.operands.PseudoAbsolute;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoAbsoluteToPseudo extends BinaryPseudoAbsoluteToPseudo {

	public MovePseudoAbsoluteToPseudo(@NotNull PseudoAbsolute source,
									  @NotNull X64PreservedRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {
		if (mapping.containsKey(source.source)) {
			if (mapping.containsKey(destination)) {
				return Collections.singletonList(
					new MoveRegAbsoluteToReg(mapping.get(source.source), mapping.get(destination))
				);
			} else {
				return Arrays.asList(
					new MoveRegAbsoluteToReg(mapping.get(source.source), temporaryImmediate),
					new MoveRegToBasePointerOffset(temporaryImmediate, locals.get(destination))
				);
			}
		} else {
			if (mapping.containsKey(destination)) {
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(locals.get(source.source), temporaryImmediate),
					new MoveRegAbsoluteToReg(temporaryImmediate, mapping.get(destination))
				);
			} else {
				// both are mapped to memory, but the first is already mapped to memory
				return Arrays.asList(
					new MoveBasePointerOffsetToReg(locals.get(source.source), temporaryImmediate),
					new MoveRegAbsoluteToReg(temporaryImmediate, temporaryImmediate),
					new MoveRegToBasePointerOffset(temporaryImmediate, locals.get(destination))
				);
			}
		}
	}
}

package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveImmToBasePointerOffsetInstruction;
import x64.instructions.MoveImmToRegInstruction;
import x64.operands.BasePointerOffset;
import x64.operands.Immediate;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoveImmToPseudo extends BinaryImmediateToPseudo {

	public MoveImmToPseudo(@NotNull Immediate source, @NotNull X64PreservedRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {
		if (mapping.containsKey(destination)) {
			return Collections.singletonList(
				new MoveImmToRegInstruction(source, mapping.get(destination))
			);
		} else {
			return Collections.singletonList(
				new MoveImmToBasePointerOffsetInstruction(source, locals.get(destination))
			);
		}
	}
}

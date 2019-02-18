package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.instructions.MoveBasePointerOffsetToReg;
import x64.instructions.MoveRegToReg;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovePseudoToReg extends BinaryPseudoToReg {

	public MovePseudoToReg(@NotNull X64PreservedRegister source,
						   @NotNull X64NativeRegister destination) {
		super("mov", source, destination);
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
														@NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
														@NotNull X64NativeRegister temporaryImmediate) {

		if (mapping.containsKey(source)) {
			return Collections.singletonList(
				new MoveRegToReg(mapping.get(source), destination)
			);
		} else {
			return Collections.singletonList(
				new MoveBasePointerOffsetToReg(locals.get(source), destination)
			);
		}
	}
}

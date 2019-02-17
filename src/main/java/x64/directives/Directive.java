package x64.directives;

import org.jetbrains.annotations.NotNull;
import x64.pseudo.PseudoInstruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** This class a directive, which is an instruction for the assembler to do, not the actual CPU when run. */
interface Directive extends PseudoInstruction {

	default	@NotNull List<@NotNull PseudoInstruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
																  @NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
																  @NotNull X64NativeRegister temporaryImmediate) {
		return Collections.singletonList(this);
	}
}

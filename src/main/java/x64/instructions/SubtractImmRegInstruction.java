package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.pseudo.BinaryInstruction;
import x64.pseudo.PseudoInstruction;
import x64.operands.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubtractImmRegInstruction extends BinaryInstruction {
	@NotNull private final Immediate source;
	@NotNull private final X64NativeRegister destination;

	public SubtractImmRegInstruction(@NotNull Immediate source, @NotNull X64NativeRegister destination) {
		super("sub", source, destination);
		this.source = source;
		this.destination = destination;
	}

	@Override
	public @NotNull List<@NotNull PseudoInstruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
															  @NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
															  @NotNull X64NativeRegister temporaryImmediate) {
		return Collections.singletonList(this);
	}
}

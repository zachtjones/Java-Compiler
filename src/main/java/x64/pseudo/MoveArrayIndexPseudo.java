package x64.pseudo;

import org.jetbrains.annotations.NotNull;
import x64.instructions.Instruction;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.List;
import java.util.Map;

/**
 * Represents mov source, (base, index, scaling).
 */
public class MoveArrayIndexPseudo implements PseudoInstruction {
	@NotNull private final X64PseudoRegister source, base, index;
	private final int scaling;

	public MoveArrayIndexPseudo(@NotNull X64PseudoRegister source, @NotNull X64PseudoRegister base,
								@NotNull X64PseudoRegister index, int scaling) {
		this.source = source;
		this.base = base;
		this.index = index;
		this.scaling = scaling;
	}

	@Override
	public @NotNull List<@NotNull Instruction> allocate(@NotNull Map<X64PseudoRegister, X64Register> mapping,
														@NotNull Map<X64PseudoRegister, BasePointerOffset> locals,
														@NotNull X64Register temporaryImmediate) {
		
	}
}

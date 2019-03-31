package x64.allocation;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Map;

public class AllocationContext {
	@NotNull private final Map<X64PseudoRegister, X64Register> mapping;
	@NotNull private final Map<X64PseudoRegister, BasePointerOffset> locals;
	@NotNull private final X64Register scratchRegisters;

	public AllocationContext(@NotNull Map<X64PseudoRegister, X64Register> mapping,
							 @NotNull Map<X64PseudoRegister, BasePointerOffset> offsets,
							 @NotNull X64Register scratchRegisters) {

		this.mapping = mapping;
		this.locals = offsets;
		this.scratchRegisters = scratchRegisters;
	}

	public boolean isRegister(X64PseudoRegister pseudo) {
		return mapping.containsKey(pseudo);
	}

	public X64Register getRegister(X64PseudoRegister pseudo) {
		return mapping.get(pseudo);
	}

	public BasePointerOffset getBasePointer(X64PseudoRegister pseudo) {
		return locals.get(pseudo);
	}
}

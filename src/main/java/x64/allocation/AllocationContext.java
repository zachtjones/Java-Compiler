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

	/**
	 * Holds the information that is required to map out the registers to hardware ones.
	 * @param mapping The registers that are mapped to hardware registers directly.
	 * @param offsets The ones that are mapped to base pointer offsets.
	 * @param scratchRegisters The scratch register to use when needed.
	 */
	AllocationContext(@NotNull Map<X64PseudoRegister, X64Register> mapping,
							 @NotNull Map<X64PseudoRegister, BasePointerOffset> offsets,
							 @NotNull X64Register scratchRegisters) {

		this.mapping = mapping;
		this.locals = offsets;
		this.scratchRegisters = scratchRegisters;
	}

	/** Utility method for determining if the pseudo get mapped to a hardware register.*/
	public boolean isRegister(X64PseudoRegister pseudo) {
		return mapping.containsKey(pseudo);
	}

	/** Utility method for determining which register it is mapped to.
	 * First check that isRegister(pseudo). */
	public X64Register getRegister(X64PseudoRegister pseudo) {
		return mapping.get(pseudo);
	}

	/** Obtains the base pointer offset that pseudo is mapped to. */
	public BasePointerOffset getBasePointer(X64PseudoRegister pseudo) {
		return locals.get(pseudo);
	}

	/** Returns the scratch register. Used when we would have 2 memory operands in one instruction. */
	public X64Register getScratchRegister() {
		return scratchRegisters;
	}
}

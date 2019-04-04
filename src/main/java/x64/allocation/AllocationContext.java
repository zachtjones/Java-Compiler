package x64.allocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.operands.BPOffset;
import x64.operands.X64PseudoRegister;
import x64.operands.X64Register;

import java.util.Map;

public class AllocationContext {
	@NotNull private final Map<X64PseudoRegister, X64Register> mapping;
	@NotNull private final Map<X64PseudoRegister, BPOffset> locals;
	@NotNull private final X64Register scratchRegister;
	@Nullable private final X64Register secondScratch;

	/**
	 * Holds the information that is required to map out the registers to hardware ones.
	 * @param mapping The registers that are mapped to hardware registers directly.
	 * @param offsets The ones that are mapped to base pointer offsets.
	 * @param scratchRegister The scratch register to use when needed.
	 * @param scratchRegister2 The second scratch register if needed.
	 */
	AllocationContext(@NotNull Map<X64PseudoRegister, X64Register> mapping,
							 @NotNull Map<X64PseudoRegister, BPOffset> offsets,
							 @NotNull X64Register scratchRegister,
					  @Nullable X64Register scratchRegister2) {

		this.mapping = mapping;
		this.locals = offsets;
		this.scratchRegister = scratchRegister;
		this.secondScratch = scratchRegister2;
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
	public BPOffset getBasePointer(X64PseudoRegister pseudo) {
		return locals.get(pseudo);
	}

	/** Returns the scratch register. Used when we would have 2 memory operands in one instruction. */
	@NotNull public X64Register getScratchRegister() {
		return scratchRegister;
	}

	/** Returns the second scratch register. Throws an exception if there aren't two */
	@NotNull public X64Register getSecondScratch() throws NotSecondScratchException {
		if (secondScratch == null) throw new NotSecondScratchException();
		return secondScratch;
	}
}

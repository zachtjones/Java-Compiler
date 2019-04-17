package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;
import x64.operands.RegIndexing;
import x64.operands.X64Register;

/** Loads the effective address of the array indexing to the register destination.
 * Note this is only defined for quad pointers. */
public class LoadEffectiveAddressIndexingToReg extends Instruction {

	public LoadEffectiveAddressIndexingToReg(@NotNull RegIndexing source, @NotNull X64Register destination) {
		super("leaq " + source + ", " + destination.assemblyRep(X64InstructionSize.QUAD));
	}
}

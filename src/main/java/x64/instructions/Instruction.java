package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.allocation.AllocationContext;
import x64.pseudo.PseudoInstruction;

import java.util.Collections;
import java.util.List;

/** Represents a real instruction, one that gcc could transform into machine code. */
public abstract class Instruction implements PseudoInstruction {

	/**
	 * All instructions using real operands can also act as a pseudo one.
	 * Due to that, we need to be able to treat this like a pseudo as well.
	 * @param context The allocation results used for information.
	 * @return A list containing just 'this'
	 */
	@Override
	@NotNull
	public List<@NotNull Instruction> allocate(@NotNull AllocationContext context) {
		return Collections.singletonList(this);
	}

	/** Returns how this should be written as a string to a file. */
	public abstract String assemblyRepresentation();

	/** Same as AssemblyRepresentation */
	public String toString() {
		return assemblyRepresentation();
	}
}

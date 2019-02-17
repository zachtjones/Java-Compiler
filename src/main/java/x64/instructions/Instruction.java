package x64.instructions;

import org.jetbrains.annotations.NotNull;
import x64.operands.BasePointerOffset;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.pseudo.PseudoInstruction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Represents a real instruction, one that gcc could transform into machine code. */
public abstract class Instruction implements PseudoInstruction {

	/**
	 * All instructions using real operands can also act as a pseudo one.
	 * Due to that, we need to be able to treat this like a pseudo as well.
	 * @param mapping A mapping of the pseudo register to a native one for those that can be.
	 * @param locals A mapping of the pseudo register to a base pointer offset for the other ones.
	 * @param temporaryImmediate A temporary register used when an instruction would use 2 memory operands.
	 * @return A list containing just 'this'
	 */
	@Override
	@NotNull
	public List<@NotNull Instruction> allocate(@NotNull Map<X64PreservedRegister, X64NativeRegister> mapping,
													   @NotNull Map<X64PreservedRegister, BasePointerOffset> locals,
													   @NotNull X64NativeRegister temporaryImmediate) {
		return Collections.singletonList(this);
	}

	/** Returns how this should be written as a string to a file. */
	public abstract String assemblyRepresentation();

	/** Same as AssemblyRepresentation */
	public final String toString() {
		return assemblyRepresentation();
	}
}

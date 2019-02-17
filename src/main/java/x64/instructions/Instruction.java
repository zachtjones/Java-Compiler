package x64.instructions;

/** Represents a real instruction, one that gcc could transform into machine code. */
public abstract class Instruction {


	/** Returns how this should be written as a string to a file. */
	public abstract String assemblyRepresentation();

	/** Same as AssemblyRepresentation */
	public final String toString() {
		return assemblyRepresentation();
	}
}

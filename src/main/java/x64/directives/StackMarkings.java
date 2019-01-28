package x64.directives;


import static x64.allocation.CallingConvention.isLinux;

public class StackMarkings implements Directive {

	/** Returns the instance of StackMarkings that should be put at the end of all assembly files
	 * to mark the stack as non-executable. */
	public final static StackMarkings instance = new StackMarkings();

	private StackMarkings(){}

	@Override
	public String toString() {
		// only add the code to mark the stack if on linux as non-execute on linux
		if (isLinux)
			return ".section .note.GNU-stack,\"\",%progbits";

		return "";
	}
}

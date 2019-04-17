package x64.directives;


import static x64.allocation.CallingConvention.isLinux;

/** Informs the assembler to mark the stacks as non-executable. */
public class StackMarkings extends Directive {

	/** Returns the instance of StackMarkings that should be put at the end of all assembly files
	 * to mark the stack as non-executable. */
	public final static StackMarkings instance = new StackMarkings();

	private StackMarkings(){
		// only add the code to mark the stack if on linux as non-execute on linux
		//  if this is assembled on mac, it won't assemble.
		super(isLinux ? ".section .note.GNU-stack,\"\",%progbits" : "");
	}
}

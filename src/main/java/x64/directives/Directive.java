package x64.directives;

import x64.instructions.Instruction;

/** This class a directive, which is an instruction for the assembler to do, not the actual CPU when run. */
public abstract class Directive extends Instruction {
	public Directive(String representation) {
		super(representation);
	}
}

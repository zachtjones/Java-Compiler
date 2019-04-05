package x64.instructions;

/** Represents the simple return instruction, which pops the return address & jumps back. */
public class Return extends Instruction {

    /** represents the instance of a return instruction.
     * Since all are identical, there's no need to create a bunch*/
    public final static Return instance = new Return();

    /** prevent creation outside this class */
    private Return() {
		super("\tret");
	}
}

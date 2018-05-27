package intermediate;

/** branch when register is not equal to 0. */
public class BranchStatementNEZ implements InterStatement {
	LabelStatement destination;
	Register r; // uses a byte register
	
	
	/** Creates a branch statement (conditional jump) when registerNum != 0. */
	public BranchStatementNEZ(LabelStatement destination, Register r) {
		this.destination = destination;
		this.r = r;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " != 0 to " + destination.name + ";";
	}
}

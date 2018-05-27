package intermediate;

/** branch when register is equal to 0. */
public class BranchStatementEQZ implements InterStatement {
	LabelStatement destination;
	Register r; // uses a byte register
	
	
	/** Creates a branch statement (conditional jump) when registerNum == 0. */
	public BranchStatementEQZ(LabelStatement destination, Register r) {
		this.destination = destination;
		this.r = r;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " == 0 to " + destination.name + ";";
	}
}

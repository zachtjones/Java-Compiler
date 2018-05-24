package intermediate;

/** branch when register is not equal to 0. */
public class BranchStatementNEZ implements InterStatement {
	LabelStatement destination;
	int registerNum; // uses a byte register
	
	
	/** Creates a branch statement (conditional jump) when registerNum != 0. */
	public BranchStatementNEZ(LabelStatement destination, int registerNum) {
		this.destination = destination;
		this.registerNum = registerNum;
	}
	
	@Override
	public String toString() {
		return "branch when " + registerNum + " != 0 to " + destination.name + ";";
	}
}

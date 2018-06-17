package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** branch when register is not equal to 0. */
public class BranchStatementTrue implements InterStatement {
	LabelStatement destination;
	Register r; // uses a byte register
	
	
	/** Creates a branch statement (conditional jump) when registerNum != 0. */
	public BranchStatementTrue(LabelStatement destination, Register r) {
		this.destination = destination;
		this.r = r;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is true to " + destination.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs) throws CompileException {
		if (r.type != Register.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.typeFull + " to boolean.");
		}
		UsageCheck.verifyDefined(r, regs);
	}
}

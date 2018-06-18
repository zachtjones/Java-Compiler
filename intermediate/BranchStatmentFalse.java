package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** branch when register is equal to 0. */
public class BranchStatmentFalse implements InterStatement {
	LabelStatement destination;
	Register r; // uses a byte register
	
	
	/** Creates a branch statement (conditional jump) when registerNum == 0. */
	public BranchStatmentFalse(LabelStatement destination, Register r) {
		this.destination = destination;
		this.r = r;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is false to " + destination.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params,
			InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs);
		if (r.type != Register.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.typeFull + " to boolean.");
		}
	}
}

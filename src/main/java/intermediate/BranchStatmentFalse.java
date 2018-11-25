package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** branch when register is equal to 0. */
public class BranchStatmentFalse implements InterStatement {
	LabelStatement destination;
	Register r; // uses a byte register
	
	private final String fileName;
	private final int line;
	
	
	/** Creates a branch statement (conditional jump) when registerNum == 0. */
	public BranchStatmentFalse(LabelStatement destination, Register r,
			String fileName, int line) {
		this.destination = destination;
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is false to " + destination.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params,
			InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (r.type != Register.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.typeFull + " to boolean.",
					this.fileName, this.line);
		}
	}
}

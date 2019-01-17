package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;

/** branch when register is not equal to 0. */
public class BranchStatementTrue implements InterStatement {
	private LabelStatement destination;
	Register r; // uses a byte register
	
	private final String fileName;
	private final int line;
	
	/** Creates a branch statement (conditional jump) when registerNum != 0. */
	public BranchStatementTrue(LabelStatement destination, Register r,
			String fileName, int line) {
		this.destination = destination;
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "branch when " + r.toString() + " is true to " + destination.name + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs,
			HashMap<String, Types> locals, HashMap<String, Types> params,
			InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (r.getType() != Types.BOOLEAN) {
			throw new CompileException("cannot convert from: " + r.getType() + " to boolean.",
					fileName, line);
		}
	}
}

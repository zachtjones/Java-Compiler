package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;

/** throw REGISTER */
public class ThrowStatement implements InterStatement {
	Register r;
	
	private final String fileName;
	private final int line;
	
	public ThrowStatement(Register r, String fileName, int line) {
		this.r = r;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "throw " + r.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		UsageCheck.verifyDefined(r, regs, fileName, line);
	}
}

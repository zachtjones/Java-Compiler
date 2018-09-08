package intermediate;

import java.util.HashMap;

import helper.CompileException;
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
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		UsageCheck.verifyDefined(r, regs, fileName, line);
	}
}

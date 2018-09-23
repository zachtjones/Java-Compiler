package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.UsageCheck;

/** return register; */
public class ReturnRegStatement implements InterStatement {
	Register r;
	
	private final String fileName;
	private final int line;
	
	public ReturnRegStatement(Register regNum, String fileName, int line) {
		this.r = regNum;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "return " + r + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (func.returnType == null) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);
		TypeChecker.canAssign(func.returnType, r.typeFull, fileName, line);
	}
}

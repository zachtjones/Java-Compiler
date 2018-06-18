package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.UsageCheck;

/** return register; */
public class ReturnRegStatement implements InterStatement {
	Register r;
	
	public ReturnRegStatement(Register regNum) {
		this.r = regNum;
	}
	
	@Override
	public String toString() {
		return "return " + r + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (func.returnType == null) {
			throw new CompileException("Can't return an expression from void function.");
		}
		UsageCheck.verifyDefined(r, regs);
		TypeChecker.subclassOrEqual(func.returnType, r.typeFull);
	}
}

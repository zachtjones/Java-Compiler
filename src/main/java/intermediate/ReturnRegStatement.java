package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.Types;
import helper.UsageCheck;

/** return register; */
public class ReturnRegStatement implements InterStatement {

	private final Register r;
	
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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		if (func.returnType == null) {
			throw new CompileException("Can't return an expression from void function.", fileName, line);
		}
		UsageCheck.verifyDefined(r, regs, fileName, line);
		TypeChecker.canAssign(func.returnType, r.getType(), fileName, line);
	}
}

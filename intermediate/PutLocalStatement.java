package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;
import helper.UsageCheck;

/** PutLocal name = %register */
public class PutLocalStatement implements InterStatement {
	Register r;
	String localName;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to use it's value
	 * @param localName The local variable to set.
	 */
	public PutLocalStatement(Register r, String localName, String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "PutLocal " + localName + " = " + r.toString() + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(r, regs, fileName, line);
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		TypeChecker.canAssign(locals.get(localName), r.typeFull, fileName, line);
	}
}

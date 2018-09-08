package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;

/** getLocal %register = name */
public class GetLocalStatement implements InterStatement {
	Register r;
	String localName;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to set
	 * @param localName The local variable to get.
	 */
	public GetLocalStatement(Register r, String localName, String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getLocal " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		TypeChecker.subclassOrEqual(locals.get(localName), r.typeFull, fileName, line);
		
		// define the register
		r.typeFull = locals.get(localName);
		regs.put(r, r.typeFull);
	}
}

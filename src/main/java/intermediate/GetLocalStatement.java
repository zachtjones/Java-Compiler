package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

/** getLocal %register = name */
public class GetLocalStatement implements InterStatement {
	Register r;
	private String localName;
	
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
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.",
					fileName, line);
		}
		
		// define the register
		r.setType(locals.get(localName));
		regs.put(r, r.getType());
	}
}

package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** getParam %register = name */
public class GetParamStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new get parameter variable statement.
	 * @param registerNum The register number to set
	 * @param localName The parameter to get.
	 */
	public GetParamStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getParam " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (!params.containsKey(localName)) {
			throw new CompileException("Error: " + localName + " not a parameter.");
		}
		
		regs.put(r, params.get(localName));
	}
}

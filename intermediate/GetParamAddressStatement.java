package intermediate;

import java.util.HashMap;

import helper.CompileException;

/** getLocalAddress %register = name */
public class GetParamAddressStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to set
	 * @param localName The local variable to get.
	 */
	public GetParamAddressStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getParamAddress " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (!params.containsKey(localName)) {
			throw new CompileException("Error: " + localName + " not a parameter.");
		}
		
		regs.put(r, params.get(localName) + "*");		
	}
}

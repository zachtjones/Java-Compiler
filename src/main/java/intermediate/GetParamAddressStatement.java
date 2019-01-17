package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;

/** getLocalAddress %register = name */
public class GetParamAddressStatement implements InterStatement {
	Register r;
	private String localName;
	
	private final String fileName;
	private final int line;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to set
	 * @param localName The local variable to get.
	 */
	public GetParamAddressStatement(Register r, String localName, String fileName, int line) {
		this.r = r;
		this.localName = localName;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "getParamAddress " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		if (!params.containsKey(localName)) {
			throw new CompileException("Error: " + localName + " not a parameter.", fileName, line);
		}
		
		regs.put(r, params.get(localName) + "*");
	}
}

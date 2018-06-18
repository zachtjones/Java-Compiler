package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.TypeChecker;

public class GetLocalAddressStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new get local variable address statement.
	 * @param r The register to set containing the address
	 * @param localName The local variable to get.
	 */
	public GetLocalAddressStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getLocalAddress " + r.toString() + " = " + localName + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		if (!locals.containsKey(localName)) {
			throw new CompileException("local variable: " + localName + " is not defined.");
		}
		TypeChecker.subclassOrEqual(locals.get(localName), r.typeFull);
		
		// define the register
		r.typeFull = locals.get(localName + "*");
		regs.put(r, r.typeFull);
	}
}

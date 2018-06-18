package intermediate;

import java.util.HashMap;

import helper.CompileException;

public interface InterStatement {
	/** Replaces all reference types with their fully qualified name 
	 * @param regs A mapping of the defined registers to their typeNames.
	 * @param locals A mapping of the defined local variables to their typeNames.
	 * @param params A mapping of the defined parameters to their typeNames.
	 * @param func The function that the statement is in.
	 * @throws CompileException If there is a type error. */
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params, 
			InterFunction func) throws CompileException;
}
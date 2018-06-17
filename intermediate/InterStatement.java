package intermediate;

import java.util.HashMap;

import helper.CompileException;

public interface InterStatement {
	/** Replaces all reference types with their fully qualified name 
	 * @throws CompileException If there is a type error. */
	public void typeCheck(HashMap<Register, String> regs) throws CompileException;
}
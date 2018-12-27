package intermediate;

import java.util.HashMap;

import helper.CompileException;
import x64.X64File;
import x64.X64Function;

public interface InterStatement {
	/** Replaces all reference types with their fully qualified name 
	 * @param regs A mapping of the defined registers to their typeNames.
	 * @param locals A mapping of the defined local variables to their typeNames.
	 * @param params A mapping of the defined parameters to their typeNames.
	 * @param func The function that the statement is in.
	 * @throws CompileException If there is a type error. */
	void typeCheck(HashMap<Register, String> regs,
			HashMap<String, String> locals, HashMap<String, String> params, 
			InterFunction func) throws CompileException;

	/**
	 * Compiles this statement down to the assembly level for x64,
	 * with the only catch being unlimited temporary registers.
	 * @param assemblyFile The assembly file for more context, used in adding data segment parts.
	 * @param function The x64 function to add the instructions to.
	 * @throws CompileException If there is an error compiling, note that this should only happen for statements
	 * not implemented yet.
	 */
	default void compile(X64File assemblyFile, X64Function function) throws CompileException {
		throw new CompileException("compiling to x64 not done for " + this, "", -1);
	}
}
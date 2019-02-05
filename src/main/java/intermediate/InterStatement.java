package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;

public interface InterStatement {
	/** Replaces all reference types with their fully qualified name 
	 * @param regs A mapping of the defined registers to their typeNames.
	 * @param locals A mapping of the defined local variables to their typeNames.
	 * @param params A mapping of the defined parameters to their typeNames.
	 * @param func The function that the statement is in.
	 * @throws CompileException If there is a type error. */
	void typeCheck(@NotNull HashMap<Register, Types> regs,
			@NotNull HashMap<String, Types> locals, @NotNull HashMap<String, Types> params,
			@NotNull InterFunction func) throws CompileException;

	/**
	 * Compiles this statement down to the assembly level for x64,
	 * with the only catch being unlimited temporary registers.
	 * @param context The context for the compilation
	 * @throws CompileException If there is an error compiling, note that this should only happen for statements
	 * not implemented yet.
	 */
	default void compile(@NotNull X64Context context) throws CompileException {
		throw new CompileException("compiling to x64 not done for " + this, "", -1);
	}
}
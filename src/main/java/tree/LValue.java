package tree;

import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** Nodes than implement this have an address, 
 * and as such can appear on the left side of an expression.*/
public interface LValue extends Expression {
	
	/** Resolves all variables and class names as symbols, and then compiles it, but
	 * the address is the last register, not the value.
	 * PrimaryExpression in the parser and rules that generate that can be LValue's.
	 * @param s The symbol table reference to the innermost scope at
	 * that point. Only null going to the Compilation unit class.
	 * @param f The intermediate function to add the code. */
    void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException;
}

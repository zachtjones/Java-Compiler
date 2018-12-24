package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public interface Node {
	/** Writes all names as a string with the fully qualified name */
    public void resolveImports(ClassLookup c) throws CompileException;
    
    /** Resolves all variables and class names as symbols, and then compiles it.
	 * @param s The symbol table reference to the innermost scope at
	 * that point. Only null going to the Compilation unit class.
	 * @param f The intermediate function to add the code. */
    public void compile(SymbolTable s, InterFunction f) throws CompileException;
    
    /** Gets the file name for this node */
    public String getFileName();
   
    /** Gets the line number this node starts on */
    public int getLine();
}

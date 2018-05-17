package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public interface Node {
	/** Writes all names as a string with the fully qualified name */
    public void resolveImports(ClassLookup c) throws IOException;
    
    /** Resolves all variables and class names as symbols.
     * @param s The symbol table reference to the innermost scope at 
     * that point. Only null going to the Compilation unit class. */
    public void resolveSymbols(SymbolTable s) throws CompileException;
}

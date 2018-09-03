package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public interface Node {
	/** Writes all names as a string with the fully qualified name */
    public void resolveImports(ClassLookup c) throws IOException;
    
    /** Resolves all variables and class names as symbols, and then compiles it.
     * @param s The symbol table reference to the innermost scope at 
     * that point. Only null going to the Compilation unit class. 
     * @param f The intermediate function to add the code. 
     * @param r The register allocator to get new numbers from. (also used for labels) 
     * @param c The CompileHistory object to use for decision making. */
    public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException;
    
    /** Gets the file name for this node */
    public String getFileName();
   
    /** Gets the line number this node starts on */
    public int getLine();
}

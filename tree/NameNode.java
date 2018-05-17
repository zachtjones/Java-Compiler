package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class NameNode implements Node {
    public String primaryName;
    public NameNode extendsNode; // used in class / interface declarations
    public ArrayList<NameNode> generics;
    public NameNode secondaryName;
    
    /** holds the scope resolved from the symbol table*/
    private int scope;
    
	/** Gets the simple name (like java.lang) for this name node, 
	 * using the secondary name if needed.
	 * Generics are not filled in */
	public String getSimpleName() {
		StringBuilder result = new StringBuilder(primaryName);
		if (secondaryName != null) {
			result.append('.');
			result.append(secondaryName.getSimpleName());
		}
		return result.toString();
	}

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// only the first part - the primaryName could be not fully qualified
		//   in the example: System.out.println
		//System.out.print("Replace: " + this.getSimpleName());
		String firstFull = c.getFullName(primaryName);
		String total = firstFull;
		if (secondaryName != null) {
			String rest = secondaryName.getSimpleName();
			total += '.' + rest;
		}
		// have resolved it all
		this.primaryName = total;
		this.secondaryName = null;
		//System.out.println(" -> " + total);
		// TODO handle generics and the ? extends / super thing
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		this.scope = s.lookup(primaryName);
		if (this.scope == -1) {
			throw new CompileException("symbol: " + primaryName + " does not exist in the symbol table.");
		}
	}
    
	/** Holds one of the constants from the SymbolTable class. */
	public int getScope() {
		return scope;
	}
}

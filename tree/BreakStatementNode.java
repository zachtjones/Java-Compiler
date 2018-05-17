package tree;

import helper.ClassLookup;
import helper.CompileException;

public class BreakStatementNode implements Node {
    public String name; // could be null - label to break

	@Override
	public void resolveImports(ClassLookup c) {
		// nothing needed
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// nothing needed, check if name is in symbol table for
		//  the compile method if name != null.
	}
}

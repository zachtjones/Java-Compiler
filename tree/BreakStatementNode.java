package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class BreakStatementNode implements Node {
    public String name; // could be null - label to break

	@Override
	public void resolveImports(ClassLookup c) {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// nothing needed, check if name is in symbol table for
		//  the compile method if name != null.
	}
}

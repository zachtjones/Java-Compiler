package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ContinueStatementNode implements Node {
    // could be null, name of loop to continue
    public String name;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// nothing needed, same as break statement thing
	}
}

package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class ContinueStatementNode implements Node {
    // could be null, name of loop to continue
    public String name;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// nothing needed, same as break statement thing
	}
}

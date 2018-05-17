package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ResultTypeNode implements Node {
    public boolean isVoid;
    public TypeNode type;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (!isVoid) type.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// nothing to do here.
	}
}

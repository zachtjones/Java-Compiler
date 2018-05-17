package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class VariableIdNode implements Node {
    public String name;
    public int numDimensions; // 0 for not array
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// nothing needed
	}
}

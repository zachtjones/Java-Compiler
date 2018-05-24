package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class VariableDecNode implements Node {
    public VariableIdNode id;
    public VariableInitializerNode init;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed with id, as it doesn't have a name node
		init.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// this is already done at higher levels in tree.
	}
    
    
}

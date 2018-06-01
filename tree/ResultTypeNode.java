package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class ResultTypeNode implements Node {
    public boolean isVoid;
    public TypeNode type;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (!isVoid) type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// nothing to do here.
	}
	
	@Override
	public String toString() {
		if (isVoid) { return "void"; }
		return type.interRep();
	}
}

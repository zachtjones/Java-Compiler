package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ParamNode implements Node {
    public TypeNode type;
    public VariableIdNode id;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		type.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// already dealt with in MethodNode or ConstructorNode.
	}
}

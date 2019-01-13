package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ResultTypeNode extends NodeImpl {
    public boolean isVoid;
    public TypeNode type;

    public ResultTypeNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		if (!isVoid) type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// nothing to do here.
	}
	
	@Override
	public String toString() {
		if (isVoid) { return "void"; }
		return type.interRep();
	}
}

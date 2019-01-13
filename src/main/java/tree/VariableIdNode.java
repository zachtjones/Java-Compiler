package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class VariableIdNode extends NodeImpl {
    public String name;
    public int numDimensions; // 0 for not array

    public VariableIdNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// nothing needed
	}
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class VariableDecNode extends NodeImpl {
    public VariableIdNode id;
    public VariableInitializerNode init;

    public VariableDecNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// nothing needed with id, as it doesn't have a name node
		init.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// this is already done at higher levels in tree.
	}
    
    
}

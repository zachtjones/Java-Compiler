package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ParamNode extends NodeImpl {
    public TypeNode type;
    public VariableIdNode id;
    public boolean isVarargs; // if it is the ...
    
    public ParamNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// already dealt with in MethodNode or ConstructorNode.
		if (this.isVarargs) {
			throw new CompileException("Error: varargs not implemented yet.", getFileName(), getLine());
		}
	}
}

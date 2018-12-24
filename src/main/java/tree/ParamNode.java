package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ParamNode implements Node {
    public TypeNode type;
    public VariableIdNode id;
    public boolean isVarargs; // if it is the ...
    
    private final String fileName;
    private final int line;
    
    public ParamNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// already dealt with in MethodNode or ConstructorNode.
		if (this.isVarargs) {
			throw new CompileException("Error: varargs not implemented yet.", fileName, line);
		}
	}
}

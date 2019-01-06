package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class SwitchLabelNode implements Node {
    public Expression expression;
    public boolean isDefault; // if default, no expression
    public String fileName;
    public int line;
    
    public SwitchLabelNode(String fileName, int line) {
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
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// TODO - check if the expression is a constant
		throw new CompileException("Switch statements not implemented yet.", fileName, line);
		//if (expression != null) expression.compile(s, 0, null);
	}
}

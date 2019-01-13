package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class SwitchLabelNode extends NodeImpl {
    public Expression expression;
    public boolean isDefault; // if default, no expression
    
    public SwitchLabelNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// TODO - check if the expression is a constant
		throw new CompileException("Switch statements not implemented yet.", getFileName(), getLine());
		//if (expression != null) expression.compile(s, 0, null);
	}
}

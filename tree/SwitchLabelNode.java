package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class SwitchLabelNode implements Node {
    public ExpressionNode expression;
    public boolean isDefault; // if default, no expression
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// TODO - check if the expression is a constant
		throw new CompileException("Switch statements not implemented yet.");
		//if (expression != null) expression.compile(s, 0, null);
	}
}

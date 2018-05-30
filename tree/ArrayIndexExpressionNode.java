package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** [ expr ] */
public class ArrayIndexExpressionNode implements Expression {
    public Expression expr;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expr.resolveImports(c);
	}

	
}

package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ReturnStatementNode implements Node {
    // could be null
    public ExpressionNode expression;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		if (expression != null) expression.resolveSymbols(s);
	}
}

package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ThrowStatementNode implements Node {
    public ExpressionNode expression;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		expression.resolveSymbols(s);
	}
}

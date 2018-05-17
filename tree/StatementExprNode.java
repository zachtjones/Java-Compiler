package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class StatementExprNode implements Node {
	public boolean isPreIncrement;
	public boolean isPreDecrement;
	public boolean isPostfixExpression;
	public boolean isAssignment;

	// this holds the expression of type from above
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

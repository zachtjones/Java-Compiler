package tree;

import java.io.IOException;

import helper.ClassLookup;

public class StatementExprNode implements Node {
	public boolean isPreIncrement;
	public boolean isPreDecrement;
	public boolean isPostfixExpression;
	public boolean isAssignment;

	// this holds the expression of type from above
	public ExpressionNode expression;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		expression.resolveNames(c);
	}

}

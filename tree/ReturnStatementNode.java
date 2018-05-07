package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ReturnStatementNode implements Node {
    // could be null
    public ExpressionNode expression;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveNames(c);
	}
}

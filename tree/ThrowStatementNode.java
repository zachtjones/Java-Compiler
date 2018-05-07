package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ThrowStatementNode implements Node {
    public ExpressionNode expression;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		expression.resolveNames(c);
	}
}

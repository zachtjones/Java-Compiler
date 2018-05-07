package tree;

import java.io.IOException;

import helper.ClassLookup;

public class DoStatementNode implements Node {
    public StatementNode statement;
    public ExpressionNode expression;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		statement.resolveNames(c);
		expression.resolveNames(c);
	}
}

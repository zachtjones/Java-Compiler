package tree;

import java.io.IOException;

import helper.ClassLookup;

public class WhileStatementNode implements Node {
    public ExpressionNode expression;
    public StatementNode statement;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		expression.resolveNames(c);
		statement.resolveNames(c);
	}
}

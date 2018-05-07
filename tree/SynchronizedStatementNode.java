package tree;

import java.io.IOException;

import helper.ClassLookup;

public class SynchronizedStatementNode implements Node {
    public ExpressionNode expression;
    public BlockNode block;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		expression.resolveNames(c);
		block.resolveNames(c);
	}
    
    
}

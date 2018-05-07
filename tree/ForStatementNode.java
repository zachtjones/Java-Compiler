package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class ForStatementNode implements Node {
    // these 3 are all optional, so could be null
    public ForInitNode init;
    public ExpressionNode condition;
    public ArrayList<StatementExprNode> update;
    // the block of code
    public StatementNode block;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		init.resolveNames(c);
		condition.resolveNames(c);
		for (StatementExprNode s : update) {
			s.resolveNames(c);
		}
		block.resolveNames(c);
	}
}

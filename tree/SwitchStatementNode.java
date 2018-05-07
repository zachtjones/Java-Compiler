package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class SwitchStatementNode implements Node {
    public ExpressionNode expression;
    // these next two are same length
    public ArrayList<SwitchLabelNode> labels;
    public ArrayList<ArrayList<BlockStatementNode> > statements;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		expression.resolveNames(c);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).resolveNames(c);
			for (BlockStatementNode b : statements.get(i)) {
				b.resolveNames(c);
			}
		}
	}
}

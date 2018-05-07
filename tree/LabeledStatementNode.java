package tree;

import java.io.IOException;

import helper.ClassLookup;

public class LabeledStatementNode implements Node {
    public String name;
    public StatementNode statement;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		statement.resolveNames(c);
	}
}

package tree;

import helper.ClassLookup;

public class BreakStatementNode implements Node {
    public String name; // could be null - label to break

	@Override
	public void resolveNames(ClassLookup c) {
		// nothing needed
	}
}

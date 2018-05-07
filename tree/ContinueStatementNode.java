package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ContinueStatementNode implements Node {
    // could be null, name of loop to continue
    public String name;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// nothing needed
	}
}

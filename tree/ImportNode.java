package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ImportNode implements Node {
    public NameNode name;
    public boolean isAll; // java.util.* would be all
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// don't resolve import node's names, that doesn't make any sense.
	}
}

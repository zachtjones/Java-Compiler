package tree;

import java.io.IOException;

import helper.ClassLookup;

public class VariableIdNode implements Node {
    public String name;
    public int numDimensions; // 0 for not array
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// nothing needed
	}
}

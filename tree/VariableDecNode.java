package tree;

import java.io.IOException;

import helper.ClassLookup;

public class VariableDecNode implements Node {
    public VariableIdNode id;
    public VariableInitializerNode init;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// nothing needed with id, as it doesn't have a name node
		init.resolveNames(c);
	}
    
    
}

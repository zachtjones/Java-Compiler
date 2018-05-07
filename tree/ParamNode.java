package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ParamNode implements Node {
    public TypeNode type;
    public VariableIdNode id;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		type.resolveNames(c);
	}
}

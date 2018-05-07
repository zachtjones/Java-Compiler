package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ResultTypeNode implements Node {
    public boolean isVoid;
    public TypeNode type;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (!isVoid) type.resolveNames(c);
	}
}

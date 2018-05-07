package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class LocalVariableDecNode implements Node {
    public TypeNode type;
    public ArrayList<VariableDecNode> declarators;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		type.resolveNames(c);
	}
}

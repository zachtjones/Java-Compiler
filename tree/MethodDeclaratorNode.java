package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class MethodDeclaratorNode implements Node {
    public String name;
    public ArrayList<ParamNode> params;
    public int arrayDims; // 0 for non array
    // arrayDims is for the return type
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		for (ParamNode p : params) {
			p.resolveNames(c);
		}
	}
}

package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class VariableInitializerNode implements Node {
    // this is if you do { VariableInitializerNodes }
    // empty otherwise
    public ArrayList<VariableInitializerNode> nextLevel = new ArrayList<>();

    // this is just a normal expression
    public ExpressionNode e;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (e != null) {
			e.resolveNames(c);
		} else {
			for (VariableInitializerNode n : nextLevel) {
				n.resolveNames(c);
			}
		}
	}
    
    
}

package tree;

import java.io.IOException;

import helper.ClassLookup;

public class SwitchLabelNode implements Node {
    public ExpressionNode expression;
    public boolean isDefault; // if default, no expression
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveNames(c);
	}
}

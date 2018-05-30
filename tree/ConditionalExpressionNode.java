package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** condition ? truePart : falsePart */
public class ConditionalExpressionNode implements Expression {
    public Expression condition;
    public Expression truePart;
    public Expression falsePart;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		condition.resolveImports(c);
		truePart.resolveImports(c);
		falsePart.resolveImports(c);
	}
	
}

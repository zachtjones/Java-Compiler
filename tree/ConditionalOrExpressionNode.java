package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** Chain of || of the operands */
public class ConditionalOrExpressionNode implements Expression {
    public ArrayList<Expression> expressions = new ArrayList<Expression>();

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

}

package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** ( expressions * )
* This is the second part of a function call, the arguments list. */
public class ArgumentExpressionNode implements Expression {
    /** The expressions to evaluate before the function call. Could be empty, but will not be null. */
    public ArrayList<Expression> expressions = new ArrayList<Expression>();

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

}

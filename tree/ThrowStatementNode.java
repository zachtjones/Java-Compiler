package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;
import intermediate.ThrowStatement;

public class ThrowStatementNode implements Node {
    public ExpressionNode expression;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// compile in the expression
		expression.compile(s, f, r);
		// throw the result of the expression.
		ThrowStatement th = new ThrowStatement(r.getLast());
		f.statements.add(th);
	}
}

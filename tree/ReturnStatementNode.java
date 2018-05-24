package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;
import intermediate.ReturnRegStatement;
import intermediate.ReturnVoidStatement;

public class ReturnStatementNode implements Node {
    // could be null
    public ExpressionNode expression;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// compile in the expression
		if (expression != null) {
			expression.compile(s, f, r);
			f.statements.add(new ReturnRegStatement(r.getLast()));
		} else {
			// just compile in the return statement.
			f.statements.add(new ReturnVoidStatement());
		}
	}
}

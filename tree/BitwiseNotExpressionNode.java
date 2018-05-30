package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;
import intermediate.UnaryOpStatement;

/** ~ expr */
public class BitwiseNotExpressionNode implements Expression {
    public Expression expr;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		expr.compile(s, f, r);
		// take bitwise not of the result.
		f.statements.add(new UnaryOpStatement(r.getLast(), r.getNext(r.getLast().type), '~'));
	}
}

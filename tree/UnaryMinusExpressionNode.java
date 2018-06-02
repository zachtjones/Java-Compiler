package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;
import intermediate.UnaryOpStatement;

/** - expr */
public class UnaryMinusExpressionNode implements Expression {
    public Expression expr;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		expr.compile(s, f, r, c);
		Register exprResult = r.getLast();
		Register result = r.getNext(exprResult.type);
		f.statements.add(new UnaryOpStatement(exprResult, result, '-'));
	}
}

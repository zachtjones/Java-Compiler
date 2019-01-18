package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.UnaryOpStatement;

/** - expr */
public class UnaryMinusExpressionNode extends NodeImpl implements Expression {
    public Expression expr;

    public UnaryMinusExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		expr.compile(s, f);
		Register exprResult = f.allocator.getLast();
		Register result = f.allocator.getNext(exprResult.getType());
		f.statements.add(new UnaryOpStatement(exprResult, result, '-', getFileName(), getLine()));
	}
}

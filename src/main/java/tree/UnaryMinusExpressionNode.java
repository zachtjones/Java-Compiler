package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.UnaryOpStatement;
import org.jetbrains.annotations.NotNull;

/** - expr */
public class UnaryMinusExpressionNode extends NodeImpl implements Expression {
    @NotNull private final Expression expr;

    public UnaryMinusExpressionNode(String fileName, int line, @NotNull Expression expression) {
    	super(fileName, line);
    	this.expr = expression;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		expr.compile(s, f);
		Register exprResult = f.allocator.getLast();
		Register result = f.allocator.getNext(exprResult.getType());
		f.addStatement(new UnaryOpStatement(exprResult, result, '-', getFileName(), getLine()));
	}
}

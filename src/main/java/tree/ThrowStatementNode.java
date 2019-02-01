package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.ThrowStatement;
import org.jetbrains.annotations.NotNull;

public class ThrowStatementNode extends NodeImpl implements StatementNode {
    public Expression expression;

    public ThrowStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// compile in the expression
		expression.compile(s, f);
		// throw the result of the expression.
		ThrowStatement th = new ThrowStatement(f.allocator.getLast(), getFileName(), getLine());
		f.statements.add(th);
	}
}

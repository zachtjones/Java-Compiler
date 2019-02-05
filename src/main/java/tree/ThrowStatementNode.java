package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.ThrowStatement;
import org.jetbrains.annotations.NotNull;

public class ThrowStatementNode extends NodeImpl implements StatementNode {

	@NotNull private final Expression expression;

    public ThrowStatementNode(String fileName, int line, @NotNull Expression expression) {
    	super(fileName, line);
    	this.expression = expression;
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
		f.statements.add(new ThrowStatement(f.allocator.getLast(), getFileName(), getLine()));
	}
}

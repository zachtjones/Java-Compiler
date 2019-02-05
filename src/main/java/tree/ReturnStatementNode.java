package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.ReturnRegStatement;
import intermediate.ReturnVoidStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReturnStatementNode extends NodeImpl implements StatementNode {
    // could be null
    @Nullable private final Expression expression;
    
    public ReturnStatementNode(String fileName, int line, @Nullable Expression expression) {
    	super(fileName, line);
    	this.expression = expression;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// compile in the expression
		if (expression != null) {
			expression.compile(s, f);
			f.statements.add(new ReturnRegStatement(f.allocator.getLast(), getFileName(), getLine()));
		} else {
			// just compile in the return statement.
			f.statements.add(new ReturnVoidStatement(getFileName(), getLine()));
		}
	}
}

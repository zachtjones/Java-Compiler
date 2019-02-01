package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.UnaryOpStatement;
import org.jetbrains.annotations.NotNull;

/** ~ expr */
public class BitwiseNotExpressionNode extends NodeImpl implements Expression {
    public Expression expr;

    public BitwiseNotExpressionNode(@NotNull String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		expr.compile(s, f);
		// take bitwise not of the result.
		f.statements.add(new UnaryOpStatement(f.allocator.getLast(),
				f.allocator.getNext(f.allocator.getLast().getType()), UnaryOpStatement.BITNOT,
			getFileName(), getLine()));
	}
}

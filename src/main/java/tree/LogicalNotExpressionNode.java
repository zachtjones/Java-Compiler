package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import intermediate.UnaryOpStatement;
import org.jetbrains.annotations.NotNull;

/** ! expr */
public class LogicalNotExpressionNode extends NodeImpl implements Expression {
    public Expression expr;
    public String fileName;
    public int line;
    
    public LogicalNotExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		expr.compile(s, f);
		f.statements.add(new UnaryOpStatement(f.allocator.getLast(),
				f.allocator.getNext(Types.BYTE), UnaryOpStatement.LOGNOT, getFileName(), getLine()));
	}
}

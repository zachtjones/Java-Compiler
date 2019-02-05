package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** synchronized (expression) block */
public class SynchronizedStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final Expression expression;
    @NotNull private final BlockNode block;

    public SynchronizedStatementNode(@NotNull String fileName, int line,
									 @NotNull Expression expression, @NotNull BlockNode block) {
    	super(fileName, line);
		this.expression = expression;
		this.block = block;
	}
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		block.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		throw new CompileException("Synchronized blocks not implemented yet.", getFileName(), getLine());
		
		/*expression.compile(s, f, r);
		block.compile(s, f, r);*/
	}
    
    
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** synchronized (expression) block */
public class SynchronizedStatementNode extends NodeImpl implements StatementNode {
    public Expression expression;
    public BlockNode block;

    public SynchronizedStatementNode(String fileName, int line) {
    	super(fileName, line);
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

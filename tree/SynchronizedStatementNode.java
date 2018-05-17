package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

/** synchronized (expression) block */
public class SynchronizedStatementNode implements Node {
    public ExpressionNode expression;
    public BlockNode block;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
		block.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		expression.resolveSymbols(s);
		block.resolveSymbols(s);
	}
    
    
}

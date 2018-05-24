package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		throw new CompileException("Synchronized blocks not implemented yet.");
		
		/*expression.compile(s, f, r);
		block.compile(s, f, r);*/
	}
    
    
}

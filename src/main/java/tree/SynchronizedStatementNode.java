package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** synchronized (expression) block */
public class SynchronizedStatementNode implements StatementNode {
    public Expression expression;
    public BlockNode block;
    public String fileName;
    public int line;
    
    public SynchronizedStatementNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		block.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		throw new CompileException("Synchronized blocks not implemented yet.", fileName, line);
		
		/*expression.compile(s, f, r);
		block.compile(s, f, r);*/
	}
    
    
}

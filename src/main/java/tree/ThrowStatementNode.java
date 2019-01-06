package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.ThrowStatement;

public class ThrowStatementNode implements StatementNode {
    public Expression expression;
    public String fileName;
    public int line;
    
    public ThrowStatementNode(String fileName, int line) {
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
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// compile in the expression
		expression.compile(s, f);
		// throw the result of the expression.
		ThrowStatement th = new ThrowStatement(f.allocator.getLast(), fileName, line);
		f.statements.add(th);
	}
}

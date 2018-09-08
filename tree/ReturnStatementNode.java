package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;
import intermediate.ReturnRegStatement;
import intermediate.ReturnVoidStatement;

public class ReturnStatementNode implements Node {
    // could be null
    public Expression expression;
    public String fileName;
    public int line;
    
    public ReturnStatementNode(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) throws IOException {
		if (expression != null) expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// compile in the expression
		if (expression != null) {
			expression.compile(s, f, r, c);
			f.statements.add(new ReturnRegStatement(r.getLast(), fileName, line));
		} else {
			// just compile in the return statement.
			f.statements.add(new ReturnVoidStatement(fileName, line));
		}
	}
}

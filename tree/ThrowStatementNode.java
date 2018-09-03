package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;
import intermediate.ThrowStatement;

public class ThrowStatementNode implements Node {
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
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// compile in the expression
		expression.compile(s, f, r, c);
		// throw the result of the expression.
		ThrowStatement th = new ThrowStatement(r.getLast());
		f.statements.add(th);
	}
}

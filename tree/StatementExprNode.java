package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class StatementExprNode implements Node {
	public boolean isPreIncrement;
	public boolean isPreDecrement;
	public boolean isPostfixExpression;
	public boolean isAssignment;
	public String fileName;
    public int line;
    
    public StatementExprNode(String fileName, int line) {
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

	// this holds the expression of type from above
	public Expression expression;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// just pass down
		expression.compile(s, f, r, c);
	}

}

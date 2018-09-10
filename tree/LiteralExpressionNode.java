package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.LoadLiteralStatement;
import intermediate.RegisterAllocator;

public class LiteralExpressionNode implements Expression {
	
    public String value;
    public String fileName;
    public int line;
    
    public LiteralExpressionNode(String fileName, int line) {
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
		// nothing to do
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// the IL code does the work here
		f.statements.add(new LoadLiteralStatement(value, r, fileName, line));	
	}
}

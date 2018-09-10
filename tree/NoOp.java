package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** Represents a do-nothing operation*/
public class NoOp implements Node, Expression {
	public String fileName;
    public int line;
    
    public NoOp(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) throws CompileException {}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, 
			CompileHistory c) throws CompileException {}

}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** Represents a do-nothing operation*/
public class NoOp implements Node, Expression, StatementNode, TypeDecNode {
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

	@Override
	public InterFile compile(String packageName, SymbolTable classLevel) throws CompileException {
		return null;
	}

}
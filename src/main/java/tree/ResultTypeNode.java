package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ResultTypeNode implements Node {
    public boolean isVoid;
    public TypeNode type;
    public String fileName;
    public int line;
    
    public ResultTypeNode(String fileName, int line) {
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
		if (!isVoid) type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// nothing to do here.
	}
	
	@Override
	public String toString() {
		if (isVoid) { return "void"; }
		return type.interRep();
	}
}

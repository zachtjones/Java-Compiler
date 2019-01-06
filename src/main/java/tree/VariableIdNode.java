package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class VariableIdNode implements Node {
    public String name;
    public int numDimensions; // 0 for not array
    public String fileName;
    public int line;
    
    public VariableIdNode(String fileName, int line) {
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
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// nothing needed
	}
}

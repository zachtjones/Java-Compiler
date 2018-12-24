package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ContinueStatementNode implements StatementNode {
    // could be null, name of loop to continue
    public String name;
    public String fileName;
    public int line;
    
    public ContinueStatementNode(String fileName, int line) {
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
		// nothing needed, same as break statement thing
		// TODO handle if there's a name
	}
}

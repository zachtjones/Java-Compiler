package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class BreakStatementNode implements StatementNode {
    public String name; // could be null - label to break
    public String fileName;
    public int line;
    
    public BreakStatementNode(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// nothing needed, check if name is in symbol table for
		//  the compile method if name != null.
	}
}

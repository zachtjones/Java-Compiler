package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** "this" */
public class ThisExpressionNode implements Expression {
	public String fileName;
    public int line;
    
    public ThisExpressionNode(String fileName, int line) {
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
		f.history.setThis();
	}

}

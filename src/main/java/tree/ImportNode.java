package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class ImportNode implements Node {
    public NameNode name;
    public boolean isAll; // java.util.* would be all
    public String fileName;
    public int line;
    
    public ImportNode(String fileName, int line) {
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
		// don't resolve import node's names, that doesn't make any sense.
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// don't need to resolve imports any more, as they are already done.
	}
}

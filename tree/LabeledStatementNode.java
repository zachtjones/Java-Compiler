package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class LabeledStatementNode implements Node {
    public String name;
    public StatementNode statement;
    public String fileName;
    public int line;
    
    public LabeledStatementNode(String fileName, int line) {
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
		statement.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// put the label into the table
		s.putEntry(name, "Label", fileName, line);
		
		// java doesn't have goto, so the only jumping of labeled statements is
		//  in break or continue statements.
		throw new CompileException("Labeled statements are not supported (yet).", fileName, line);
	}
}

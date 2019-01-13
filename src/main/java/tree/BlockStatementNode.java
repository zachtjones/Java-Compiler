package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class BlockStatementNode extends NodeImpl {
    // only one of these is not null
    public StatementNode statement;
    public LocalVariableDecNode dec;

    public BlockStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// pass down
    	if (statement != null) {
    		statement.resolveImports(c);
    	} else {
    		dec.resolveImports(c);
    	}
	}
    
    @Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// pass down -- local variable declaration puts an entry
    	if (statement != null) {
    		statement.compile(s, f);
    	} else {
    		dec.compile(s, f);
    	}
		
	}
    
}

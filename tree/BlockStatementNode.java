package tree;

import java.io.IOException;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class BlockStatementNode implements Node {
    // only one of these is not null
    public StatementNode statement;
    public LocalVariableDecNode dec;
    
    @Override
	public void resolveImports(ClassLookup c) throws IOException {
		// pass down
    	if (statement != null) {
    		statement.resolveImports(c);
    	} else {
    		dec.resolveImports(c);
    	}
	}
    
    @Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// pass down -- local variable declaration puts an entry
    	if (statement != null) {
    		statement.compile(s, f, r, c);
    	} else {
    		dec.compile(s, f, r, c);
    	}
		
	}
    
}

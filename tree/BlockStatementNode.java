package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterStatement;

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
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// pass down -- local variable declaration puts an entry
    	if (statement != null) {
    		statement.resolveSymbols(s);
    	} else {
    		dec.resolveSymbols(s);
    	}
		
	}

	public ArrayList<InterStatement> compile() throws CompileException {
		// TODO
		throw new CompileException("Block statement node compile not implemented.");
	}

	
    
}

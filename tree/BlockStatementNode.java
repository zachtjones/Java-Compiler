package tree;

import java.io.IOException;

import helper.ClassLookup;

public class BlockStatementNode implements Node {
    // only one of these is not null
    public StatementNode statement;
    public LocalVariableDecNode dec;
    
    @Override
	public void resolveNames(ClassLookup c) throws IOException {
		// pass down
    	if (statement != null) {
    		statement.resolveNames(c);
    	} else {
    		dec.resolveNames(c);
    	}
	}
    
}

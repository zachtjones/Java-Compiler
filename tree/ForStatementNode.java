package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class ForStatementNode implements Node {
    // these 3 are all optional, so could be null
    public ForInitNode init;
    public ExpressionNode condition;
    public ArrayList<StatementExprNode> update;
    // the block of code
    public StatementNode block;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		init.resolveImports(c);
		condition.resolveImports(c);
		for (StatementExprNode s : update) {
			s.resolveImports(c);
		}
		block.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// create a new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		// put in the inits into the new table
		init.resolveSymbols(newTable);
		// resolve symbol references in the condition
		condition.resolveSymbols(newTable);
		for (StatementExprNode stmt : update) {
			stmt.resolveSymbols(newTable);
		}
	}
}

package tree;

import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.EndScopeStatement;
import intermediate.InterFunction;

public class BlockNode extends NodeImpl implements StatementNode {
    public ArrayList<BlockStatementNode> statements;

    public BlockNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// pass down
		for (BlockStatementNode b : statements) {
			b.resolveImports(c);
		}
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// create new scope for variables
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		// compile with this new table, placing declarations where needed.
		for (BlockStatementNode b : statements) {
			b.compile(newTable, f);
		}
		// remove all new symbols from the table
		HashMap<String, String> entries = s.getCurrentEntries();
		entries.forEach((name, type) ->
			f.statements.add(new EndScopeStatement(name))
		);
	}

	
}

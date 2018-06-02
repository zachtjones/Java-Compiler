package tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.EndScopeStatement;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class BlockNode implements Node {
    public ArrayList<BlockStatementNode> statements;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// pass down
		for (BlockStatementNode b : statements) {
			b.resolveImports(c);
		}
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// create new scope for variables
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		// compile with this new table, placing declarations where needed.
		for (BlockStatementNode b : statements) {
			b.compile(newTable, f, r, c);
		}
		// remove all new symbols from the table
		HashMap<String, String> entries = s.getCurrentEntries();
		entries.forEach((name, type) -> {
			f.statements.add(new EndScopeStatement(name));
		});
	}

	
}

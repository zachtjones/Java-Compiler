package tree;

import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.EndScopeStatement;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class BlockNode implements StatementNode {
    public ArrayList<BlockStatementNode> statements;
    public String fileName;
    public int line;
    
    public BlockNode(String fileName, int line) {
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

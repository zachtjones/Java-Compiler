package tree;

import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.EndScopeStatement;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class BlockNode extends NodeImpl implements StatementNode {
    @NotNull private final ArrayList<BlockStatementNode> statements;

    public BlockNode(@NotNull String fileName, int line, @NotNull ArrayList<BlockStatementNode> statements) {
    	super(fileName, line);
    	this.statements = statements;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// pass down
		for (BlockStatementNode b : statements) {
			b.resolveImports(c);
		}
	}
	
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// create new scope for variables
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		// compile with this new table, placing declarations where needed.
		for (BlockStatementNode b : statements) {
			b.compile(newTable, f);
		}
		// remove all new symbols from the table
		HashMap<String, Types> entries = s.getCurrentEntries();
		entries.forEach((name, type) ->
			f.statements.add(new EndScopeStatement(name))
		);
	}

	
}

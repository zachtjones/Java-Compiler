package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterStatement;

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
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// pass down, but with new scope, as blocks create scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		for (BlockStatementNode b : statements) {
			b.resolveSymbols(newTable);
		}
	}

	public ArrayList<InterStatement> compile() throws CompileException {
		throw new CompileException("Block node compiling not implemented yet.");
		// TODO Auto-generated method stub
	}

	
}

package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class IfStatementNode implements Node {
    public ExpressionNode expression;
    public StatementNode statement;
    public StatementNode elsePart;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
		statement.resolveImports(c);
		if (elsePart != null) {
			elsePart.resolveImports(c);
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// create new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		expression.resolveSymbols(newTable);
		statement.resolveSymbols(newTable);
		if (elsePart != null) {
			elsePart.resolveSymbols(newTable);
		}
	}
}

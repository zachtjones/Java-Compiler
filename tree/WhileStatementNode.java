package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class WhileStatementNode implements Node {
    public ExpressionNode expression;
    public StatementNode statement;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
		statement.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		expression.resolveSymbols(s);
		statement.resolveSymbols(s);
	}
}

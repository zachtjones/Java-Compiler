package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

/** do { statement } while (expression); */
public class DoStatementNode implements Node {
    public StatementNode statement;
    public ExpressionNode expression;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		statement.resolveImports(c);
		expression.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		statement.resolveSymbols(s);
		expression.resolveSymbols(s);
	}
}

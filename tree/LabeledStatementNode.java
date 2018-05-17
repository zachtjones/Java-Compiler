package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class LabeledStatementNode implements Node {
    public String name;
    public StatementNode statement;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		statement.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// put the label into the table
		s.putEntry(name, "Label");
		statement.resolveSymbols(s);
	}
}

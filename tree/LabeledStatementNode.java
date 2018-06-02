package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class LabeledStatementNode implements Node {
    public String name;
    public StatementNode statement;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		statement.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// put the label into the table
		s.putEntry(name, "Label");
		
		// java doesn't have goto, so the only jumping of labeled statements is
		//  in break or continue statements.
		throw new CompileException("Labeled statements are not supported (yet).");
	}
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class LabeledStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final String name;
    @NotNull private final StatementNode statement;
    
    public LabeledStatementNode(@NotNull String fileName, int line, @NotNull String name,
								@NotNull StatementNode statement) {
    	super(fileName, line);
    	this.name = name;
    	this.statement = statement;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		statement.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
    	// any statement can have a label, but will only be useful for loops
		// put the label into the table
		s.putEntry(name, Types.LABEL, getFileName(), getLine());
		s.markStatementAsLabeled(statement, name);

		// add in the statement
		statement.compile(s, f);
	}
}

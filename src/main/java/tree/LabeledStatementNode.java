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
		// put the label into the table
		s.putEntry(name, Types.LABEL, getFileName(), getLine());
		
		// java doesn't have goto, so the only jumping of labeled statements is
		//  in break or continue statements.
		throw new CompileException("Labeled statements are not supported (yet).", getFileName(), getLine());
	}
}

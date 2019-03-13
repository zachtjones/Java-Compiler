package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BreakStatementNode extends NodeImpl implements StatementNode {
    @Nullable private final String name; // label to break

    public BreakStatementNode(@NotNull String fileName, int line, @Nullable String name) {
    	super(fileName, line);
    	this.name = name;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) {
		// nothing needed
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

    	// get the destination from the table, throws CompileEx if not set
		LabelStatement destination = s.getBreakLabel(getFileName(), getLine());

		// jump to that label
		f.addStatement(new JumpStatement(destination));
	}
}

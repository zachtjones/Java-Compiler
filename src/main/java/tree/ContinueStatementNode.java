package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContinueStatementNode extends NodeImpl implements StatementNode {
    @Nullable private final String name; // label to continue

    public ContinueStatementNode(@NotNull String fileName, int line, @Nullable String name) {
    	super(fileName, line);
		this.name = name;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		throw new CompileException("continue not implemented yet", "", -1);
	}
}

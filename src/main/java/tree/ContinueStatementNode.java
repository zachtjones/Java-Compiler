package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class ContinueStatementNode extends NodeImpl implements StatementNode {
    // could be null, name of loop to continue
    public String name;

    public ContinueStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// nothing needed, same as break statement thing
		// TODO handle if there's a name
	}
}

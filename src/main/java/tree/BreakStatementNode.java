package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class BreakStatementNode extends NodeImpl implements StatementNode {
    public String name; // could be null - label to break

    public BreakStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) {
		// nothing needed
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// nothing needed, check if name is in symbol table for
		//  the compile method if name != null.
	}
}

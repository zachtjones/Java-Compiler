package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** Represents a do-nothing operation*/
public class NoOp extends NodeImpl implements Expression, StatementNode, TypeDecNode {
    
    public NoOp(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {}

	@Override
	public InterFile compile(String packageName, SymbolTable classLevel) throws CompileException {
		return null;
	}

}

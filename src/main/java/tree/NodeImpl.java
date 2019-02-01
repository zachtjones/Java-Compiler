package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the partial implementation of a Node in the parse tree.
 * This NodeImpl class provides a common way to store the line number and filename currently being parsed.
 */
public abstract class NodeImpl implements Node {

	@NotNull private final String fileName;
	private final int line;

	protected NodeImpl(@NotNull String fileName, int line) {
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public abstract void resolveImports(@NotNull ClassLookup c) throws CompileException;

	@Override
	public abstract void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException;

	@NotNull
	@Override
	public final String getFileName() {
		return this.fileName;
	}

	@Override
	public final int getLine() {
		return this.line;
	}
}

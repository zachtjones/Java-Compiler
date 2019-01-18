package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/**
 * Represents the partial implementation of a Node in the parse tree.
 * This NodeImpl class provides a common way to store the line number and filename currently being parsed.
 */
public abstract class NodeImpl implements Node {

	private final String fileName;
	private final int line;

	protected NodeImpl(String fileName, int line) {
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public abstract void resolveImports(ClassLookup c) throws CompileException;

	@Override
	public abstract void compile(SymbolTable s, InterFunction f) throws CompileException;

	@Override
	public final String getFileName() {
		return this.fileName;
	}

	@Override
	public final int getLine() {
		return this.line;
	}
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/** Represents a do-nothing operation*/
public class NoOp implements Expression, StatementNode, TypeDecNode {

	private final int line;
	@NotNull private final String fileName;

	public NoOp(@NotNull String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {}

	@Override
	public @NotNull String getFileName() {
		return fileName;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public InterFile compile(@Nullable String packageName, @NotNull SymbolTable classLevel) throws CompileException {
		return new InterFile("not_used", "java/lang/Object", new ArrayList<>());
	}

}

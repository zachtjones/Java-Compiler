package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SwitchStatementPart extends NodeImpl  {
	@NotNull private final ArrayList<BlockStatementNode> statements;
	@NotNull private final SwitchLabelNode label;

	public SwitchStatementPart(@NotNull String fileName, int line,
								  @NotNull SwitchLabelNode label,
								  @NotNull ArrayList<BlockStatementNode> statements) {
		super(fileName, line);
		this.statements = statements;
		this.label = label;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (BlockStatementNode statement : statements) {
			statement.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		throw new CompileException("Switch statement compiling not implemented yet.", getFileName(), getLine());
	}
}

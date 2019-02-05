package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/** Represents a list of statement expression nodes.
 * This can be used in the init part of for(init; condition; inc) ...*/
public class StatementExprNodeList extends NodeImpl implements ForInitNode {

	@NotNull private final ArrayList<StatementExprNode> list;

	public StatementExprNodeList(@NotNull String fileName, int line, ArrayList<StatementExprNode> list) {
		super(fileName, line);
		this.list = list;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (StatementExprNode node : list) {
			node.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		for (StatementExprNode node : list) {
			node.compile(s, f);
		}
	}
}

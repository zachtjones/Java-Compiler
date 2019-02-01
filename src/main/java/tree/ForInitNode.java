package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class ForInitNode extends NodeImpl {
	// either this, or the second
	public LocalVariableDecNode dec;
	// or this:
	public ArrayList<StatementExprNode> items;

    public ForInitNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		if (dec != null) {
			dec.resolveImports(c);
		} else {
			for (StatementExprNode s : items) {
				s.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		if (dec != null) {
			dec.compile(s, f);
		} else {
			for (StatementExprNode s1 : items) {
				s1.compile(s, f);
			}
		}
		
	}
}

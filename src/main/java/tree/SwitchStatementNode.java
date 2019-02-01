package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class SwitchStatementNode extends NodeImpl implements StatementNode {
    public Expression expression;
    // these next two are same length
    public ArrayList<SwitchLabelNode> labels;
    public ArrayList<ArrayList<BlockStatementNode> > statements;
    
    public SwitchStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).resolveImports(c);
			for (BlockStatementNode b : statements.get(i)) {
				b.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {
		
		throw new CompileException("Switch statement compiling not implemented yet.", getFileName(), getLine());
		
		/*expression.compile(s, 0, null);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).compile(s, 0, null);
			for (BlockStatementNode b : statements.get(i)) {
				b.compile(s, 0, null);
			}
		}*/
	}
}

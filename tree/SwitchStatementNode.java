package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class SwitchStatementNode implements Node {
    public ExpressionNode expression;
    // these next two are same length
    public ArrayList<SwitchLabelNode> labels;
    public ArrayList<ArrayList<BlockStatementNode> > statements;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).resolveImports(c);
			for (BlockStatementNode b : statements.get(i)) {
				b.resolveImports(c);
			}
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		expression.resolveSymbols(s);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).resolveSymbols(s);
			for (BlockStatementNode b : statements.get(i)) {
				b.resolveSymbols(s);
			}
		}
	}
}

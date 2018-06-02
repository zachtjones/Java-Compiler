package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class SwitchStatementNode implements Node {
    public Expression expression;
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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		throw new CompileException("Switch statement compiling not implemented yet.");
		
		/*expression.compile(s, 0, null);
		for (int i = 0; i < labels.size(); i++) {
			labels.get(i).compile(s, 0, null);
			for (BlockStatementNode b : statements.get(i)) {
				b.compile(s, 0, null);
			}
		}*/
	}
}

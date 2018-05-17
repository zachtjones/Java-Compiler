package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class VariableInitializerNode implements Node {
    /** this is if you do { VariableInitializerNodes }
    * empty otherwise */
    public ArrayList<VariableInitializerNode> nextLevel = new ArrayList<>();

    /** this is just a normal expression */
    public ExpressionNode e;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (e != null) {
			e.resolveImports(c);
		} else {
			for (VariableInitializerNode n : nextLevel) {
				n.resolveImports(c);
			}
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		if (e != null) {
			e.resolveSymbols(s);
		} else {
			for (VariableInitializerNode n : nextLevel) {
				n.resolveSymbols(s);
			}
		}
	}
    
    
}

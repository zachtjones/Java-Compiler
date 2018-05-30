package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class VariableInitializerNode implements Node {
    /** this is if you do { VariableInitializerNodes }
    * empty otherwise */
    public ArrayList<VariableInitializerNode> nextLevel = new ArrayList<>();

    /** this is just a normal expression */
    public Expression e;

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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		if (e != null) {
			e.compile(s, f, r);
		} else {
			throw new CompileException("array initializer expressions with items not implemented.");
		}
	}
    
    
}

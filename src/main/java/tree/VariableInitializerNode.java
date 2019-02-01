package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class VariableInitializerNode extends NodeImpl {
    /** this is if you do { VariableInitializerNodes }
    * empty otherwise */
    public ArrayList<VariableInitializerNode> nextLevel = new ArrayList<>();

    /** this is just a normal expression */
    public Expression e;

    public VariableInitializerNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		if (e != null) {
			e.resolveImports(c);
		} else {
			for (VariableInitializerNode n : nextLevel) {
				n.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		if (e != null) {
			e.compile(s, f);
		} else {
			// TODO
			throw new CompileException("array initializer expressions with items not implemented.",
					getFileName(), getLine());
		}
	}
    
    
}

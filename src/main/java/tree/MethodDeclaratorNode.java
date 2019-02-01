package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class MethodDeclaratorNode extends NodeImpl {
    public String name;
    public ArrayList<ParamNode> params;
    public int arrayDims; // 0 for non array
    // arrayDims is for the return type
    
    public MethodDeclaratorNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (ParamNode p : params) {
			p.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

		// s is for the parameters, place them in s
		for (ParamNode p : params) {
			s.putEntry(p.id.name, p.type, getFileName(), getLine());
			f.paramTypes.add(p.type);
			f.paramNames.add(p.id.name);
			if (p.isVarargs) {
				if (f.lastArgVarargs) { // can only have one argument varargs, and as to be last
					throw new CompileException(
							"the variable arguments can only be used on the last parameter.",
							getFileName(), getLine());
				}
				f.lastArgVarargs = true;
			} else {
				if (f.lastArgVarargs)
					// make sure that a previous argument was not ...
					throw new CompileException("the variable arguments can onlybe on the last paramter.",
						getFileName(), getLine());
			}
		}
	}
}

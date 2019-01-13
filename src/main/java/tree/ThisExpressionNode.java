package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** "this" */
public class ThisExpressionNode extends NodeImpl implements Expression {

    public ThisExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		f.history.setThis();
	}

}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;

/** (type[]...)expr */
public class ObjectCastExpressionNode extends NodeImpl implements Expression {
    public Expression expr;
    public Types type;
    public int arrayDims = 0;
    
    public ObjectCastExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
		type.resolveImports(c, getFileName(), getLine());
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// TODO
		throw new CompileException("Object cast not implemented yet.", getFileName(), getLine());
	}

}

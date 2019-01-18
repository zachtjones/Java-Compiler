package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;

/** (type[]...)expr */
public class PrimitiveCastExpressionNode extends NodeImpl implements Expression {
    public Expression expr;
    public Types type;
    public int arrayDims = 0;
    
    public PrimitiveCastExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }
    
    @Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
		type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// TODO
		throw new CompileException("Primitive cast not implemented yet.", getFileName(), getLine());
	}
}

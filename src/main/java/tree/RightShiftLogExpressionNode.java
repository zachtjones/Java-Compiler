package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** left >>> right (0 filled) */
public class RightShiftLogExpressionNode extends NodeImpl implements Expression {
    public Expression left;
    public Expression right;
    
    public RightShiftLogExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		// result is of type left
		Register result = f.allocator.getNext(leftResult.type);
		f.statements.add(new BinaryOpStatement(
				leftResult, rightResult, result, (char)BinaryOpStatement.RSHIFTLOG, getFileName(), getLine()));
	}
}

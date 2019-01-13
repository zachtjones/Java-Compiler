package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** left + right */
public class AddExpressionNode extends NodeImpl implements Expression {
    public Expression left;
    public Expression right;
    
    public AddExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// evaluate left
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		// evaluate right
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		// add them
		Register destination = f.allocator.getNext(Register.getLarger(leftResult.type, rightResult.type));
		f.statements.add(new BinaryOpStatement(leftResult, rightResult, destination, '+',
			getFileName(), getLine()));
	}
	
}

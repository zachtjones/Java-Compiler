package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.SetConditionStatement;

/** left <= right */
public class LessThanEqualExpressionNode extends NodeImpl implements Expression {
    public Expression left;
    public Expression right;

    public LessThanEqualExpressionNode(String fileName, int line) {
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
		
		// add in the condition
		Register result = f.allocator.getNext(Register.BOOLEAN);
		f.statements.add(new SetConditionStatement(
			SetConditionStatement.LESSEQUAL, leftResult, rightResult, result, getFileName(), getLine()));
	}
}

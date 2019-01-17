package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.SetConditionStatement;

/** left != right */
public class NotEqualityExpressionNode extends NodeImpl implements Expression {
    public Expression left;
    public Expression right;
    
    public NotEqualityExpressionNode(String fileName, int line) {
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
		
		Register result = f.allocator.getNext(Types.BYTE);
		
		// do a compare
		f.statements.add(new SetConditionStatement(
				SetConditionStatement.NOTEQUAL, leftResult, rightResult, result, getFileName(), getLine()));
	}
}

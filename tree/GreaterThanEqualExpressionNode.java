package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;
import intermediate.SetConditionStatement;

/** left >= right */
public class GreaterThanEqualExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		left.resolveImports(c);
		right.resolveImports(c);
	}
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		left.compile(s, f, r, c);
		Register leftResult = r.getLast();
		
		right.compile(s, f, r, c);
		Register rightResult = r.getLast();
		
		// add in the condition
		Register result = r.getNext(Register.BYTE);
		f.statements.add(new SetConditionStatement(
			SetConditionStatement.GREATEREQUAL, leftResult, 
				rightResult, result));
	}
}

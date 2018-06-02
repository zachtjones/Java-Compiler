package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** left >> right (sign filled)*/
public class RightShiftArithExpressionNode implements Expression {
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
		
		// result is of type left
		Register result = r.getNext(leftResult.type);
		f.statements.add(new BinaryOpStatement(
				leftResult, rightResult, result, 
				(char)BinaryOpStatement.RSHIFTARITH));
	}
}

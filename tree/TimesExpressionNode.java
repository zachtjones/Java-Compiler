package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** left * right */
public class TimesExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		left.resolveImports(c);
		right.resolveImports(c);		
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// evaluate left
		left.compile(s, f, r);
		Register leftResult = r.getLast();
		// evaluate right
		right.compile(s, f, r);
		Register rightResult = r.getLast();

		// * them
		Register destination = r.getNext(Register.getLarger(leftResult.type, rightResult.type));
		f.statements.add(new BinaryOpStatement(leftResult, rightResult, destination, '*'));
	}
}

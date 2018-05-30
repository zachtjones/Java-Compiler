package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** Chain of ^ of the operands (not short-circuiting, aka bitwise or also)*/
public class ExclusiveOrExpressionNode implements Expression {
    public ArrayList<Expression> expressions = new ArrayList<Expression>();

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// compile in the first one
		expressions.get(0).compile(s, f, r);

		// use its result in the next one
		Register result = r.getLast();
		for (int i = 1; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f, r);
			// add in XOR of the last two
			Register current = result;
			Register two = r.getLast();
			result = r.getNext(Register.getLarger(current.type, two.type));
			// add the XOR statement
			f.statements.add(new BinaryOpStatement(current, two, result, '^'));
		}
	}
}

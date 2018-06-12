package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.CallVirtualStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** ( expressions * )
* This is the second part of a function call, the arguments list. */
public class ArgumentExpressionNode implements Expression {
    /** The expressions to evaluate before the function call. Could be empty, but will not be null. */
    public ArrayList<Expression> expressions = new ArrayList<Expression>();

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		
		String name = c.getName();
		// have to go back 2 registers
		Register obj = r.get2Before();
		
		// remove the getInstanceField part, and get the identifier
		f.statements.remove(f.statements.size() - 1);
		
		// compile in the args
		Register[] result = new Register[expressions.size()];
		for(int i = 0; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f, r, c);
			result[i] = r.getLast();
		}
		
		// add in the call virtual statement
		f.statements.add(new CallVirtualStatement(obj, name, result, r.getNext(Register.REFERENCE)));
	}

}

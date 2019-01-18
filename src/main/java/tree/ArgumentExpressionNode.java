package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.CallVirtualStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** ( expressions * )
* This is the second part of a function call, the arguments list. */
public class ArgumentExpressionNode extends NodeImpl implements Expression {
    /** The expressions to evaluate before the function call. Could be empty, but will not be null. */
    public ArrayList<Expression> expressions = new ArrayList<>();

    public ArgumentExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		for (Expression e : expressions) {
			e.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		
		String name = f.history.getName();
		// have to go back 2 registers
		Register obj = f.allocator.get2Before();
		
		// remove the getInstanceField part, and get the identifier
		f.statements.remove(f.statements.size() - 1);
		
		// compile in the args
		Register[] result = new Register[expressions.size()];
		for(int i = 0; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f);
			result[i] = f.allocator.getLast();
		}
		
		// add in the call virtual statement
		f.statements.add(new CallVirtualStatement(obj, name, result,
				f.allocator.getNext(Types.UNKNOWN), getFileName(), getLine()));
	}

}

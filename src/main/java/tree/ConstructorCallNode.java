package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.AllocateClassMemoryStatement;
import intermediate.CallVirtualStatement;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** new Name (args) */
public class ConstructorCallNode extends NodeImpl implements Expression {
    public NameNode name;
    public ArgumentExpressionNode args;

    public ConstructorCallNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		name.resolveImports(c);
		args.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		Register result = f.allocator.getNext(name.primaryName);

		ArrayList<Expression> expressions = args.expressions;
		// compile in the args
		Register[] results = new Register[expressions.size()];
		for(int i = 0; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f);
			results[i] = f.allocator.getLast();
		}
		
		// allocate memory
		f.statements.add(new AllocateClassMemoryStatement(name.primaryName, result));
		
		// copy
		Register finalResult = f.allocator.getNext(result.type);
		
		// add in the call virtual statement
		f.statements.add(new CallVirtualStatement(result, "<init>", results, null,
			getFileName(), getLine()));
		
		// result is the finalResult
		f.statements.add(new CopyStatement(result, finalResult, getFileName(), getLine()));
		
	}
}

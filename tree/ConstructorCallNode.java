package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.AllocateClassMemoryStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** new Name (args) */
public class ConstructorCallNode implements Expression {
    public NameNode name;
    public ArgumentExpressionNode args;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		name.resolveImports(c);
		args.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		Register result = r.getNext(name.primaryName);
		// allocate memory
		f.statements.add(new AllocateClassMemoryStatement(name.primaryName, result));
		
		// call the init function -- add in "this" pointer first
		ArgumentExpressionNode call = new ArgumentExpressionNode();
		call.expressions = new ArrayList<Expression>();
		call.expressions.add(result);
		call.expressions.addAll(args.expressions);
		call.compile(s, f, r, c);
	}
}

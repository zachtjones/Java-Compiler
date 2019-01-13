package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetArrayValueStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** [ expr ] */
public class ArrayIndexExpressionNode extends NodeImpl implements Expression, LValue {
    public Expression expr;

    public ArrayIndexExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		Register array = f.allocator.getLast();
		// get the index
		expr.compile(s, f);
		Register index = f.allocator.getLast();
		// load the memory at the address
		Register result = f.allocator.getNext("unknown");
		f.statements.add(new GetArrayValueStatement(array, index, result, getFileName(), getLine()));
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f)
			throws CompileException {
		
		Register array = f.allocator.getLast();
		// get the index
		expr.compile(s, f);
		Register index = f.allocator.getLast();
		// load the memory at the address
		Register result = f.allocator.getNext("unknown");
		f.statements.add(new GetArrayValueStatement(array, index, result, getFileName(), getLine()));
	}

	
}

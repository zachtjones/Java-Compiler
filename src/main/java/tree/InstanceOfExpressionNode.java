package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InstanceOfStatement;
import intermediate.InterFunction;
import intermediate.Register;

/* left instanceof right */
public class InstanceOfExpressionNode extends NodeImpl implements Expression {
    public Expression left;
    public Types right;
    
    public InstanceOfExpressionNode(String fileName, int line) {
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
		Register value = f.allocator.getLast();
		// test if value is the instance of the class
		String className = right.getClassName(getFileName(), getLine());
		
		Register result = f.allocator.getNext(Types.BOOLEAN);
		
		f.statements.add(new InstanceOfStatement(value, className, result, getFileName(), getLine()));
	}
}

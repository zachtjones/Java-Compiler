package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InstanceOfStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/* left instanceof right */
public class InstanceOfExpressionNode extends NodeImpl implements Expression {
	@NotNull private final Expression left;
    @NotNull private final Types right;
    
    public InstanceOfExpressionNode(String fileName, int line, @NotNull Expression left, @NotNull Types right) {
    	super(fileName, line);
    	this.left = left;
    	this.right = right;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c, getFileName(), getLine());
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		left.compile(s, f);
		Register value = f.allocator.getLast();
		// test if value is the instance of the class
		String className = right.getClassName(getFileName(), getLine());
		
		Register result = f.allocator.getNext(Types.BOOLEAN);
		
		f.statements.add(new InstanceOfStatement(value, className, result, getFileName(), getLine()));
	}
}

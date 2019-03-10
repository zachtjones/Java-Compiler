package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.GetArrayValueStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/** object [ expr ].  Object could be an array index operation itself  */
public class ArrayIndexExpressionNode extends NodeImpl implements Expression, LValue {

	@NotNull private final Expression object;
    @NotNull private final Expression expr;

    public ArrayIndexExpressionNode(@NotNull Expression object, @NotNull Expression expr,
									@NotNull String fileName, int line) {
    	super(fileName, line);
    	this.object = object;
    	this.expr = expr;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
    	object.resolveImports(c);
		expr.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
    	object.compile(s, f);
		Register array = f.allocator.getLast();
		// get the index
		expr.compile(s, f);
		Register index = f.allocator.getLast();
		// load the memory at the address
		Register result = f.allocator.getNext(Types.UNKNOWN);
		f.statements.add(new GetArrayValueStatement(array, index, result, getFileName(), getLine()));
	}

	@Override
	public void compileAddress(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {

		object.compile(s, f);
		Register array = f.allocator.getLast();
		// get the index
		expr.compile(s, f);
		Register index = f.allocator.getLast();
		// load the memory at the address
		Register result = f.allocator.getNext(Types.UNKNOWN);
		f.statements.add(new GetArrayValueStatement(array, index, result, getFileName(), getLine()));
	}

	
}

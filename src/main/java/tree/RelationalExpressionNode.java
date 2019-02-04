package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.SetConditionStatement;
import org.jetbrains.annotations.NotNull;

/** left relational operator right;
 * relational operator is one of: less than, greater than, less than or equal, greater than or equal */
public class RelationalExpressionNode extends NodeImpl implements Expression {
	@NotNull private final Expression left, right;
	private final int type; // one of SetConditionStatement constants

    public RelationalExpressionNode(String fileName, int line,
									@NotNull Expression left, @NotNull Expression right, int type) {
    	super(fileName, line);
    	this.left = left;
    	this.right = right;
    	this.type = type;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		// add in the condition
		Register result = f.allocator.getNext(Types.BOOLEAN);
		f.statements.add(new SetConditionStatement(type, leftResult, rightResult, result, getFileName(), getLine()));
	}
}

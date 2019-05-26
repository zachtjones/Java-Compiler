package tree;

import helper.BinaryOperation;
import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;

/** expr ++ */
public class PostIncrementExpressionNode extends NodeImpl implements StatementExprNode, Expression {
    public Expression expr;
    
    public PostIncrementExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

    	// evaluate expression
    	expr.compile(s, f);
    	Register result = f.allocator.getLast();

    	// add one to it
		f.addStatement(new LoadLiteralStatement("1", f.allocator, getFileName(), getLine()));
		Register one = f.allocator.getLast();

		Register plusOne = f.allocator.getNext(Types.UNKNOWN);
		f.addStatement(new BinaryOpStatement(result, one, plusOne, BinaryOperation.ADD, getFileName(), getLine()));

		// assign plusOne to the expression's address
		AssignmentNode n = new AssignmentNode(getFileName(), getLine(), expr, plusOne, null);

		// compile it
		n.compile(s, f);

		// need the result to be the last created register, so need to do a copy
		f.addStatement(
			new CopyStatement(result, f.allocator.getNext(result.getType()), getFileName(), getLine())
		);
	}

}

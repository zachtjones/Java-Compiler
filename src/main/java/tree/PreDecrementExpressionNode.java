package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.LoadLiteralStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import static helper.BinaryOperation.SUBTRACT;

/** -- expr */
public class PreDecrementExpressionNode extends NodeImpl implements StatementExprNode, Expression {
    public Expression expr;

    public PreDecrementExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);		
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// get new one = expr
		// expr -- (but have to use the previous answer - 1, can't calculate 2x)
		// copy new new one from new one
		expr.compile(s, f);
		Register result = f.allocator.getLast();
		
		// subtract 1
		f.statements.add(new LoadLiteralStatement("1", f.allocator, getFileName(), getLine()));
		Register one = f.allocator.getLast();
		
		f.statements.add(new BinaryOpStatement(result, one, f.allocator.getNext(result.getType()), SUBTRACT,
			getFileName(), getLine()));
		Register minusOne = f.allocator.getLast();
		// compile in the store to the address
		if (!(expr instanceof LValue)) {
			throw new CompileException("Can't assign the expression.", getFileName(), getLine());
		}
		((LValue)expr).compileAddress(s, f);
		// store it back
		f.statements.add(new CopyStatement(minusOne, f.allocator.getLast(), getFileName(), getLine()));
		
		// result is before the subtraction
		f.statements.add(new CopyStatement(result, f.allocator.getNext(result.getType()), getFileName(), getLine()));
	}
}

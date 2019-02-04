package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.BranchStatementFalse;
import intermediate.ChooseStatement;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/** left && right */
public class ConditionalAndExpressionNode extends NodeImpl implements Expression {
    @NotNull private final Expression left, right;

    public ConditionalAndExpressionNode(String fileName, int line, @NotNull Expression left, @NotNull Expression right) {
    	super(fileName, line);
		this.left = left;
		this.right = right;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		left.compile(s, f);
		LabelStatement end = new LabelStatement("L_" + f.allocator.getNextLabel());
		Register leftResult = f.allocator.getLast();
		// if left is false, jump to end
		f.statements.add(new BranchStatementFalse(end, leftResult, getFileName(), getLine()));
		
		// compile in right half
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		f.statements.add(end);

		// keep in SSA, need choose statement
		Register newReg = f.allocator.getNext(Types.BOOLEAN); // result is boolean
		f.statements.add(new ChooseStatement(leftResult, rightResult, newReg, getFileName(), getLine()));
	}
}

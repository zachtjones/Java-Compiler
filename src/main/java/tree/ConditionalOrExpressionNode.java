package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.BranchStatementTrue;
import intermediate.ChooseStatement;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

/** Chain of || of the operands */
public class ConditionalOrExpressionNode extends NodeImpl implements Expression {
	public Expression left;
	public Expression right;

    public ConditionalOrExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {
		
		left.compile(s, f);
		LabelStatement end = new LabelStatement("L_" + f.allocator.getNextLabel());
		Register leftResult = f.allocator.getLast();
		// if left is true, jump to end
		f.statements.add(new BranchStatementTrue(end, leftResult, getFileName(), getLine()));
		
		// compile in right half
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		f.statements.add(end);

		// keep in SSA, need choose statement
		Register newReg = f.allocator.getNext(Types.BOOLEAN); // result is boolean
		f.statements.add(new ChooseStatement(leftResult, rightResult, newReg, getFileName(), getLine()));
	}

}

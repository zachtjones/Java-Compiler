package tree;

import java.io.IOException;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementTrue;
import intermediate.ChooseStatement;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** Chain of || of the operands */
public class ConditionalOrExpressionNode implements Expression {
	public Expression left;
	public Expression right;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		left.compile(s, f, r, c);
		LabelStatement end = new LabelStatement("L_" + r.getNextLabel());
		Register leftResult = r.getLast();
		// if left is true, jump to end
		f.statements.add(new BranchStatementTrue(end, leftResult));
		
		// compile in right half
		right.compile(s, f, r, c);
		Register rightResult = r.getLast();
		
		f.statements.add(end);

		// keep in SSA, need choose statement
		Register newReg = r.getNext(Register.BOOLEAN); // result is boolean
		f.statements.add(new ChooseStatement(leftResult, rightResult, newReg));
	}

}

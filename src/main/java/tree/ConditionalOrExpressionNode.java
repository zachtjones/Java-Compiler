package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementTrue;
import intermediate.ChooseStatement;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.Register;

/** Chain of || of the operands */
public class ConditionalOrExpressionNode implements Expression {
	public Expression left;
	public Expression right;
	public String fileName;
    public int line;
    
    public ConditionalOrExpressionNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f)
			throws CompileException {
		
		left.compile(s, f);
		LabelStatement end = new LabelStatement("L_" + f.allocator.getNextLabel());
		Register leftResult = f.allocator.getLast();
		// if left is true, jump to end
		f.statements.add(new BranchStatementTrue(end, leftResult, fileName, line));
		
		// compile in right half
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();
		
		f.statements.add(end);

		// keep in SSA, need choose statement
		Register newReg = f.allocator.getNext(Register.BOOLEAN); // result is boolean
		f.statements.add(new ChooseStatement(leftResult, rightResult, newReg, fileName, line));
	}

}

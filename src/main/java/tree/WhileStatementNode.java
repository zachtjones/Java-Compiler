package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatmentFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;

public class WhileStatementNode extends NodeImpl implements StatementNode {
    public Expression expression;
    public StatementNode statement;

    public WhileStatementNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		statement.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// label for expression
		LabelStatement exprLbl = new LabelStatement("L_COND_" + f.allocator.getNextLabel());
		// label for ending
		LabelStatement endLbl = new LabelStatement("L_END_" + f.allocator.getNextLabel());
		
		// add in the label for expression
		f.statements.add(exprLbl);
		// compile in expression
		expression.compile(s, f);
		
		// if false, goto end
		f.statements.add(new BranchStatmentFalse(endLbl, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the block
		statement.compile(s, f);
		
		// unconditional jump to the expression
		f.statements.add(new JumpStatement(exprLbl));
		
		// add in the ending label
		f.statements.add(endLbl);
	}
}

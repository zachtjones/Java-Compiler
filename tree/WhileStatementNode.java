package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatmentFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import intermediate.RegisterAllocator;

public class WhileStatementNode implements Node {
    public Expression expression;
    public StatementNode statement;
    public String fileName;
    public int line;
    
    public WhileStatementNode(String fileName, int line) {
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
		expression.resolveImports(c);
		statement.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// label for expression
		LabelStatement exprLbl = new LabelStatement("L_COND_" + r.getNextLabel());
		// label for ending
		LabelStatement endLbl = new LabelStatement("L_END_" + r.getNextLabel());
		
		// add in the label for expression
		f.statements.add(exprLbl);
		// compile in expression
		expression.compile(s, f, r, c);
		
		// if false, goto end
		f.statements.add(new BranchStatmentFalse(endLbl, r.getLast(), fileName, line));
		
		// compile in the block
		statement.compile(s, f, r, c);
		
		// unconditional jump to the expression
		f.statements.add(new JumpStatement(exprLbl));
		
		// add in the ending label
		f.statements.add(endLbl);
	}
}

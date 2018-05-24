package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementEQZ;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import intermediate.RegisterAllocator;

public class WhileStatementNode implements Node {
    public ExpressionNode expression;
    public StatementNode statement;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		expression.resolveImports(c);
		statement.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// label for expression
		LabelStatement exprLbl = new LabelStatement("L_COND_" + r.getNum());
		// label for ending
		LabelStatement endLbl = new LabelStatement("L_END_" + r.getNum());
		
		// add in the label for expression
		f.statements.add(exprLbl);
		// compile in expression
		expression.compile(s, f, r);
		
		// if false, goto end
		f.statements.add(new BranchStatementEQZ(endLbl, r.getLast()));
		
		// compile in the block
		statement.compile(s, f, r);
		
		// unconditional jump to the expression
		f.statements.add(new JumpStatement(exprLbl));
		
		// add in the ending label
		f.statements.add(endLbl);
	}
}

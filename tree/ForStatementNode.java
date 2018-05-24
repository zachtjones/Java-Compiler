package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementEQZ;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.RegisterAllocator;

public class ForStatementNode implements Node {
    // these 3 are all optional, so could be null
    public ForInitNode init;
    public ExpressionNode condition;
    public ArrayList<StatementExprNode> update;
    // the block of code
    public StatementNode block;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		init.resolveImports(c);
		condition.resolveImports(c);
		for (StatementExprNode s : update) {
			s.resolveImports(c);
		}
		block.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// create a new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		
		// put in the initializers first
		init.compile(newTable, f, r);
		
		// label condition -> condition -> if false, branch end 
		//   -> block -> jump condition -> label end
		LabelStatement conditionLabel = new LabelStatement("L_COND_" + r.getNum());
		LabelStatement endLabel = new LabelStatement("L_END_" + r.getNum());
		
		// add in condition label
		f.statements.add(conditionLabel);
		
		// compile in the condition
		condition.compile(newTable, f, r);
		
		// conditional branch to end
		// if false (zero) take the branch.
		f.statements.add(new BranchStatementEQZ(endLabel, r.getLast()));
		
		// compile in the body
		block.compile(newTable, f, r);
		
		// TODO continue should have a label here to go to.
		// compile in the update
		for (StatementExprNode stmt : update) {
			stmt.compile(newTable, f, r);
		}
		
		// add in the ending label
		f.statements.add(endLabel);
	}
}

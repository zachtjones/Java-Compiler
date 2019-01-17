package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementFalse;
import intermediate.InterFunction;
import intermediate.LabelStatement;

public class ForStatementNode extends NodeImpl implements StatementNode {
    // these 3 are all optional, so could be null
    public ForInitNode init;
    public Expression condition;
    public ArrayList<StatementExprNode> update;
    // the block of code
    public StatementNode block;
    
    public ForStatementNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		init.resolveImports(c);
		condition.resolveImports(c);
		for (StatementExprNode s : update) {
			s.resolveImports(c);
		}
		block.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// create a new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		
		// put in the initializers first
		init.compile(newTable, f);
		
		// label condition -> condition -> if false, branch end 
		//   -> block -> jump condition -> label end
		LabelStatement conditionLabel = new LabelStatement("L_COND_" + f.allocator.getNextLabel());
		LabelStatement endLabel = new LabelStatement("L_END_" + f.allocator.getNextLabel());
		
		// add in condition label
		f.statements.add(conditionLabel);
		
		// compile in the condition
		condition.compile(newTable, f);
		
		// conditional branch to end
		// if false (zero) take the branch.
		f.statements.add(new BranchStatementFalse(endLabel, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the body
		block.compile(newTable, f);
		
		// TODO continue should have a label here to go to.
		// compile in the update
		for (StatementExprNode stmt : update) {
			stmt.compile(newTable, f);
		}
		
		// add in the ending label
		f.statements.add(endLabel);
	}
}

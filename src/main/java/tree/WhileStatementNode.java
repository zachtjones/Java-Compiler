package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;

public class WhileStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final Expression expression;
    @NotNull private final StatementNode statement;

    public WhileStatementNode(@NotNull String fileName, int line, @NotNull Expression expression,
							  @NotNull StatementNode statement) {
    	super(fileName, line);
    	this.expression = expression;
    	this.statement = statement;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		statement.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
    	// create new symbol table for this loop, the break and continue destinations are specific
		//   to this loop

		SymbolTable loopTable = new SymbolTable(s, SymbolTable.local);

		// label for expression
		LabelStatement exprLbl = new LabelStatement("L_COND_" + f.allocator.getNextLabel());
		// label for ending
		LabelStatement endLbl = new LabelStatement("L_END_" + f.allocator.getNextLabel());

		// mark them in the symbol table
		loopTable.setBreakLabel(endLbl);
		loopTable.setContinueLabel(exprLbl);
		if (s.thisStatementIsLabeled(this)) {
			loopTable.setBreakLabel(endLbl, s.getLabelForThisStatement(this));
			loopTable.setContinueLabel(exprLbl, s.getLabelForThisStatement(this));
		}
		
		// add in the label for expression
		f.addStatement(exprLbl);
		// compile in expression
		expression.compile(loopTable, f);
		
		// if false, goto end
		f.addStatement(new BranchStatementFalse(endLbl, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the block
		statement.compile(loopTable, f);
		
		// unconditional jump to the expression
		f.addStatement(new JumpStatement(exprLbl));
		
		// add in the ending label
		f.addStatement(endLbl);

		// loopTable falls out of scope
		loopTable.endScope(f);
	}
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForStatementNode extends NodeImpl implements StatementNode {

    @Nullable private final ForInitNode init;
    @NotNull private final Expression condition;
    @Nullable private final StatementExprNodeList update;
    // the block of code
    @NotNull private final StatementNode block;
    
    public ForStatementNode(String fileName, int line, @Nullable ForInitNode init, @Nullable Expression condition,
							@Nullable StatementExprNodeList update, @NotNull StatementNode block) {
    	super(fileName, line);
    	this.init = init;
    	this.condition = condition == null ? new LiteralExpressionNode(fileName, line, "true") : condition;
    	this.update = update;
    	this.block = block;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		if (init != null) init.resolveImports(c);
		condition.resolveImports(c);
		if (update != null) update.resolveImports(c);
		block.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// create a new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		
		// put in the initializers first
		if (init != null) init.compile(newTable, f);
		
		// label condition -> condition -> if false, branch end 
		//   -> block -> jump condition -> label end
		LabelStatement conditionLabel = new LabelStatement("L_COND_" + f.allocator.getNextLabel());
		LabelStatement endLabel = new LabelStatement("L_END_" + f.allocator.getNextLabel());
		
		// add in condition label
		f.addStatement(conditionLabel);
		
		// compile in the condition
		condition.compile(newTable, f);
		
		// conditional branch to end
		// if false (zero) take the branch.
		f.addStatement(new BranchStatementFalse(endLabel, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the body
		block.compile(newTable, f);
		
		// TODO continue should have a label here to go to.
		// compile in the update
		if (update != null) update.compile(newTable, f);

		// jump to condition label
		f.addStatement(new JumpStatement(conditionLabel));
		
		// add in the ending label
		f.addStatement(endLabel);

		// newTable is done, add in the end scope statements
		newTable.endScope(f);
	}
}

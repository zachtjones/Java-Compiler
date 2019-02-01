package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;

public class IfStatementNode extends NodeImpl implements StatementNode {
    public Expression expression;
    public StatementNode statement;
    public StatementNode elsePart;
    
    public IfStatementNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		statement.resolveImports(c);
		if (elsePart != null) {
			elsePart.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// create new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		
		LabelStatement elseLbl = new LabelStatement("L_ELSE" + f.allocator.getNextLabel());
		LabelStatement endLbl = new LabelStatement("L_END" + f.allocator.getNextLabel());
		
		// if expression == 0, goto else
		// start with the expression
		expression.compile(newTable, f);
		// branch if == 0 to else (false)
		f.statements.add(new BranchStatementFalse(elseLbl, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the true part
		statement.compile(newTable, f);
		// true part -> jump to end
		f.statements.add(new JumpStatement(endLbl));
		
		// add in else label
		f.statements.add(elseLbl);
		// compile in the else part
		if (elsePart != null) {
			elsePart.compile(newTable, f);
		}
		
		// add in end label
		f.statements.add(endLbl);
	}
}

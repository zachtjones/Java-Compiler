package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IfStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final Expression expression;
    @NotNull private final StatementNode statement;
    @Nullable private final StatementNode elsePart;
    
    public IfStatementNode(@NotNull String fileName, int line, @NotNull Expression expression,
						   @NotNull StatementNode statement, @Nullable StatementNode elsePart) {
    	super(fileName, line);
    	this.expression = expression;
    	this.statement = statement;
    	this.elsePart = elsePart;
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
		f.addStatement(new BranchStatementFalse(elseLbl, f.allocator.getLast(), getFileName(), getLine()));
		
		// compile in the true part
		statement.compile(newTable, f);
		// true part -> jump to end
		f.addStatement(new JumpStatement(endLbl));
		
		// add in else label
		f.addStatement(elseLbl);
		// compile in the else part
		if (elsePart != null) {
			elsePart.compile(newTable, f);
		}
		
		// add in end label
		f.addStatement(endLbl);
	}
}

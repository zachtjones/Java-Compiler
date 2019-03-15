package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementTrue;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import org.jetbrains.annotations.NotNull;

/** do { statement } while (expression); */
public class DoStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final StatementNode statement;
    @NotNull private final Expression expression;

    public DoStatementNode(@NotNull String fileName, int line, @NotNull StatementNode statement,
						   @NotNull Expression expression) {

    	super(fileName, line);
    	this.statement = statement;
    	this.expression = expression;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		statement.resolveImports(c);
		expression.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
    	// introduce new scope for break & continue
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);

		// labels used for the loop
		LabelStatement startLabel = new LabelStatement("L_DO_WHILE_START" + f.allocator.getNextLabel());
		LabelStatement endLabel = new LabelStatement("L_DO_WHILE_END" + f.allocator.getNextLabel());

		// mark the labels in the table
		newTable.setContinueLabel(startLabel);
		newTable.setBreakLabel(endLabel);
		if (s.thisStatementIsLabeled(this)) {
			newTable.setContinueLabel(startLabel, s.getLabelForThisStatement(this));
			newTable.setBreakLabel(endLabel, s.getLabelForThisStatement(this));
		}

		// label at top of statement
		f.addStatement(startLabel);

		// then follows the statement
		statement.compile(newTable, f);

		// immediately followed by expression
		expression.compile(newTable, f);

		// conditional jump to top of statement
		f.addStatement(new BranchStatementTrue(startLabel, f.allocator.getLast(), getFileName(), getLine()));

		// end label, only jumped to on break, fallthrough will happen otherwise when the loop is done
		f.addStatement(endLabel);
	}
}

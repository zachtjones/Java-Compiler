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
		// label at top of statement
		LabelStatement l = new LabelStatement("L_L" + f.allocator.getNextLabel());
		f.addStatement(l);
		// then follows the statement
		statement.compile(s, f);
		// immediately followed by expression
		expression.compile(s, f);
		// conditional jump to top of statement
		f.addStatement(new BranchStatementTrue(l, f.allocator.getLast(), getFileName(), getLine()));
	}
}

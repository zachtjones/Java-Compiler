package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementNEZ;
import intermediate.InterFunction;
import intermediate.LabelStatement;
import intermediate.RegisterAllocator;

/** do { statement } while (expression); */
public class DoStatementNode implements Node {
    public StatementNode statement;
    public Expression expression;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		statement.resolveImports(c);
		expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// label at top of statement
		LabelStatement l = new LabelStatement("L_L" + r.getNextLabel());
		f.statements.add(l);
		// then follows the statement
		statement.compile(s, f, r);
		// immediately followed by expression
		expression.compile(s, f, r);
		// conditional jump to top of statement
		f.statements.add(new BranchStatementNEZ(l, r.getLast()));
	}
}

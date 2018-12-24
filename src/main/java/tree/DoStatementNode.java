package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatementTrue;
import intermediate.InterFunction;
import intermediate.LabelStatement;

/** do { statement } while (expression); */
public class DoStatementNode implements StatementNode {
    public StatementNode statement;
    public Expression expression;
    public String fileName;
    public int line;
    
    public DoStatementNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		statement.resolveImports(c);
		expression.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// label at top of statement
		LabelStatement l = new LabelStatement("L_L" + f.allocator.getNextLabel());
		f.statements.add(l);
		// then follows the statement
		statement.compile(s, f);
		// immediately followed by expression
		expression.compile(s, f);
		// conditional jump to top of statement
		f.statements.add(new BranchStatementTrue(l, f.allocator.getLast(), fileName, line));
	}
}

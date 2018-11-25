package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BranchStatmentFalse;
import intermediate.InterFunction;
import intermediate.JumpStatement;
import intermediate.LabelStatement;
import intermediate.RegisterAllocator;

public class IfStatementNode implements StatementNode {
    public Expression expression;
    public StatementNode statement;
    public StatementNode elsePart;
    public String fileName;
    public int line;
    
    public IfStatementNode(String fileName, int line) {
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
		expression.resolveImports(c);
		statement.resolveImports(c);
		if (elsePart != null) {
			elsePart.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// create new scope
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);
		
		LabelStatement elseLbl = new LabelStatement("L_ELSE" + r.getNextLabel());
		LabelStatement endLbl = new LabelStatement("L_END" + r.getNextLabel());
		
		// if expression == 0, goto else
		// start with the expression
		expression.compile(newTable, f, r, c);
		// branch if == 0 to else (false)
		f.statements.add(new BranchStatmentFalse(elseLbl, r.getLast(), fileName, line));
		
		// compile in the true part
		statement.compile(newTable, f, r, c);
		// true part -> jump to end
		f.statements.add(new JumpStatement(endLbl));
		
		// add in else label
		f.statements.add(elseLbl);
		// compile in the else part
		if (elsePart != null) {
			elsePart.compile(newTable, f, r, c);
		}
		
		// add in end label
		f.statements.add(endLbl);
	}
}

package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;
import intermediate.SetConditionStatement;

/** left > right */
public class GreaterThanExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    public String fileName;
    public int line;
    
    public GreaterThanExpressionNode(String fileName, int line) {
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
		left.resolveImports(c);
		right.resolveImports(c);
	}
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		left.compile(s, f, r, c);
		Register leftResult = r.getLast();
		
		right.compile(s, f, r, c);
		Register rightResult = r.getLast();
		
		// add in the condition
		Register result = r.getNext(Register.BOOLEAN);
		f.statements.add(new SetConditionStatement(
			SetConditionStatement.GREATER, leftResult, rightResult, result, fileName, line));
	}
}
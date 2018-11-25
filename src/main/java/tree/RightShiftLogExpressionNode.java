package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** left >>> right (0 filled) */
public class RightShiftLogExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    public String fileName;
    public int line;
    
    public RightShiftLogExpressionNode(String fileName, int line) {
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
		
		// result is of type left
		Register result = r.getNext(leftResult.type);
		f.statements.add(new BinaryOpStatement(
				leftResult, rightResult, result, (char)BinaryOpStatement.RSHIFTLOG, fileName, line));
	}
}
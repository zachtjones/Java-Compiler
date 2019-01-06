package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** left * right */
public class TimesExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    public String fileName;
    public int line;
    
    public TimesExpressionNode(String fileName, int line) {
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
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// evaluate left
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		// evaluate right
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();

		// * them
		Register destination = f.allocator.getNext(Register.getLarger(leftResult.type, rightResult.type));
		f.statements.add(new BinaryOpStatement(leftResult, rightResult, destination, '*', fileName, line));
	}
}

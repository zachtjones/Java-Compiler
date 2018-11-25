package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** left % right */
public class ModExpressionNode implements Expression {
    public Expression left;
    public Expression right;
    public String fileName;
    public int line;
    
    public ModExpressionNode(String fileName, int line) {
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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) 
			throws CompileException {
		
		// evaluate left
		left.compile(s, f, r, c);
		Register leftResult = r.getLast();
		// evaluate right
		right.compile(s, f, r, c);
		Register rightResult = r.getLast();

		// add them
		Register destination = r.getNext(Register.getLarger(leftResult.type, rightResult.type));
		f.statements.add(new BinaryOpStatement(leftResult, rightResult, destination, '%', fileName, line));
	}
}

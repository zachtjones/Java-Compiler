package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InstanceOfStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/* left instanceof right */
public class InstanceOfExpressionNode implements Expression {
    public Expression left;
    public TypeNode right;
    
    private final String fileName;
    private final int line;
    
    public InstanceOfExpressionNode(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) throws IOException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		left.compile(s, f, r, c);
		Register value = r.getLast();
		// test if value is the instance of the class
		String className = right.getILRep();
		
		Register result = r.getNext(Register.BOOLEAN);
		
		f.statements.add(new InstanceOfStatement(value, className, result, fileName, line));
	}
}

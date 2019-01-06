package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.CreateArrayStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** new type[expression or empty] ... */
public class PrimitiveArrayAllocationNode implements Expression {
    public PrimitiveTypeNode type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
    public String fileName;
    public int line;
    
    public PrimitiveArrayAllocationNode(String fileName, int line) {
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
		for (Expression e : expressions) {
			if (e != null) {
				e.resolveImports(c);
			}
		}
	}
    
	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		if (expressions.get(0) == null) {
			throw new CompileException(
				"The first dimension of an array has to have a size given at the constructor.",
				fileName, line);
		}
		if (expressions.size() == 1) {
			expressions.get(0).compile(s, f);
			Register size = f.allocator.getLast();
			Register result = f.allocator.getNext("unknown");
			f.statements.add(new CreateArrayStatement(size, type.toString(), result));
		} else {
			// TODO handle multi-dimensional arrays.
			throw new CompileException("Multi-dimensional array creation not done yet.", "", -1);
		}
	}
}

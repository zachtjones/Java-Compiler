package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetArrayValueStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** [ expr ] */
public class ArrayIndexExpressionNode implements Expression, LValue {
    public Expression expr;
    public String fileName;
    public int line;
    
    public ArrayIndexExpressionNode(String fileName, int line) {
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
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		Register array = r.getLast();
		// get the index
		expr.compile(s, f, r, c);
		Register index = r.getLast();
		// load the memory at the address
		Register result = r.getNext("unknown");
		f.statements.add(new GetArrayValueStatement(array, index, result));
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c)
			throws CompileException {
		
		Register array = r.getLast();
		// get the index
		expr.compile(s, f, r, c);
		Register index = r.getLast();
		// load the memory at the address
		Register result = r.getNext("unknown");
		f.statements.add(new GetArrayValueStatement(array, index, result));
	}

	
}

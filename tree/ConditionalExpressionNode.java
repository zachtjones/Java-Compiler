package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** condition ? truePart : falsePart */
public class ConditionalExpressionNode implements Expression {
    public Expression condition;
    public Expression truePart;
    public Expression falsePart;
    public String fileName;
    public int line;
    
    public ConditionalExpressionNode(String fileName, int line) {
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
		condition.resolveImports(c);
		truePart.resolveImports(c);
		falsePart.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) 
			throws CompileException {
		
		// TODO
		throw new CompileException("conditional ternary not implemented yet", fileName, line);		
	}
	
}

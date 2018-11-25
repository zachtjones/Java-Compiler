package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** (type[]...)expr */
public class PrimitiveCastExpressionNode implements Expression {
    public Expression expr;
    public PrimitiveTypeNode type;
    public int arrayDims = 0;
    public String fileName;
    public int line;
    
    public PrimitiveCastExpressionNode(String fileName, int line) {
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
		expr.resolveImports(c);
		type.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// TODO
		throw new CompileException("Primitive cast not implemented yet.", fileName, line);
	}
}

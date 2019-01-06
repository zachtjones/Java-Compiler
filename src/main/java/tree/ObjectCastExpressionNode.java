package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** (type[]...)expr */
public class ObjectCastExpressionNode implements Expression {
    public Expression expr;
    public NameNode type;
    public int arrayDims = 0;
    public String fileName;
    public int line;
    
    public ObjectCastExpressionNode(String fileName, int line) {
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
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// TODO
		throw new CompileException("Object cast not implemented yet.", fileName, line);
	}

}

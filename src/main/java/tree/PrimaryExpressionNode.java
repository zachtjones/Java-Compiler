package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

public class PrimaryExpressionNode implements Expression, LValue {
    public Expression prefix;
    public ArrayList<Expression> suffixes = new ArrayList<Expression>();
    public String fileName;
    public int line;
    
    public PrimaryExpressionNode(String fileName, int line) {
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
		prefix.resolveImports(c);
		for (Expression e : suffixes) {
			e.resolveImports(c);
		}
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		prefix.compile(s, f);
		for (Expression e : suffixes) {
			e.compile(s, f);
		}
		
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f) throws CompileException {
		// compile the address of the last part
		prefix.compile(s, f);
		for (int i = 0; i < suffixes.size(); i++) {
			if (i == suffixes.size() - 1) { // last one
				if (suffixes.get(i) instanceof LValue) {
					( (LValue) suffixes.get(i) ).compileAddress(s, f);
				} else {
					throw new CompileException("left side of = expression not able to assign to. " 
						+ suffixes.get(i), fileName, line);
				}
			} else {
				suffixes.get(i).compile(s, f);
			}
		}
	}
}

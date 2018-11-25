package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** "super" . fieldName 
 * Note that fields are not inherited, need a way to qualify in IL, 
 * thinking of having a 'super' pointer in the structures. 
 * */
public class SuperFieldExpressionNode implements Expression {
    public String fieldName;
    
    private final String fileName;
    private final int line;
    
    public SuperFieldExpressionNode(String fileName, int line) {
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
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// TODO
		throw new CompileException("super.field not implemented yet", fileName, line);
	}
}

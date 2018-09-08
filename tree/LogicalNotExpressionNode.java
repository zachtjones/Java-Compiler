package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;
import intermediate.UnaryOpStatement;

/** ! expr */
public class LogicalNotExpressionNode implements Expression {
    public Expression expr;
    public String fileName;
    public int line;
    
    public LogicalNotExpressionNode(String fileName, int line) {
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
		expr.compile(s, f, r, c);
		f.statements.add(new UnaryOpStatement(r.getLast(), 
				r.getNext(Register.BYTE), UnaryOpStatement.LOGNOT, fileName, line));
	}
}

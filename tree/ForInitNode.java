package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class ForInitNode implements Node {
	// either this, or the second
	public LocalVariableDecNode dec;
	// or this:
	public ArrayList<StatementExprNode> items;
	public String fileName;
    public int line;
    
    public ForInitNode(String fileName, int line) {
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
		if (dec != null) {
			dec.resolveImports(c);
		} else {
			for (StatementExprNode s : items) {
				s.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		if (dec != null) {
			dec.compile(s, f, r, c);
		} else {
			for (StatementExprNode s1 : items) {
				s1.compile(s, f, r, c);
			}
		}
		
	}
}

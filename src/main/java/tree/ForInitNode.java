package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

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
	public void resolveImports(ClassLookup c) throws CompileException {
		if (dec != null) {
			dec.resolveImports(c);
		} else {
			for (StatementExprNode s : items) {
				s.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		if (dec != null) {
			dec.compile(s, f);
		} else {
			for (StatementExprNode s1 : items) {
				s1.compile(s, f);
			}
		}
		
	}
}

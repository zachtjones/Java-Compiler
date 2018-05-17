package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class ForInitNode implements Node {
	// either this, or the second
	public LocalVariableDecNode dec;
	// or this:
	public ArrayList<StatementExprNode> items;

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
	public void resolveSymbols(SymbolTable s) throws CompileException {
		if (dec != null) {
			dec.resolveSymbols(s);
		} else {
			for (StatementExprNode s1 : items) {
				s1.resolveSymbols(s);
			}
		}
		
	}
}

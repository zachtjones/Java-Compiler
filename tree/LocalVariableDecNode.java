package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class LocalVariableDecNode implements Node {
    public TypeNode type;
    public ArrayList<VariableDecNode> declarators;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		type.resolveImports(c);
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// place in symbol table
		for (VariableDecNode d : declarators) {
			s.putEntry(d.id.name, type.interRep());
		}
	}
}

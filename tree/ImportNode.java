package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

public class ImportNode implements Node {
    public NameNode name;
    public boolean isAll; // java.util.* would be all
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// don't resolve import node's names, that doesn't make any sense.
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// don't need to resolve imports any more, as they are already done.
	}
}

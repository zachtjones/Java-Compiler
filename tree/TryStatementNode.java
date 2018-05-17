package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;

public class TryStatementNode implements Node {
	
    public BlockNode block;
    // these two are same size;
    public ArrayList<ParamNode> catchParams;
    public ArrayList<BlockNode> catchBlocks;
    
    public BlockNode finallyPart; // optional

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		block.resolveImports(c);
		for (int i = 0; i < catchParams.size(); i++) {
			catchParams.get(i).resolveImports(c);
			catchBlocks.get(i).resolveImports(c);
		}
		if (finallyPart != null) {
			finallyPart.resolveImports(c);
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		block.resolveSymbols(s);
		for (int i = 0; i < catchParams.size(); i++) {
			catchParams.get(i).resolveSymbols(s);
			catchBlocks.get(i).resolveSymbols(s);
		}
		if (finallyPart != null) {
			finallyPart.resolveSymbols(s);
		}
		
	}
    
    
}

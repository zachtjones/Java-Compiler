package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class TryStatementNode implements Node {
	
    public BlockNode block;
    // these two are same size;
    public ArrayList<ParamNode> catchParams;
    public ArrayList<BlockNode> catchBlocks;
    
    public BlockNode finallyPart; // optional
    public String fileName;
    public int line;
    
    public TryStatementNode(String fileName, int line) {
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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		throw new CompileException("try statements not implemented yet.", fileName, line);
		// TODO
	}
    
    
}

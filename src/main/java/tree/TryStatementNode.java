package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class TryStatementNode extends NodeImpl implements StatementNode {
	
    public BlockNode block;
    // these two are same size;
    public ArrayList<ParamNode> catchParams;
    public ArrayList<BlockNode> catchBlocks;
    
    public BlockNode finallyPart; // optional

    public TryStatementNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
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
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		throw new CompileException("try statements not implemented yet.", getFileName(), getLine());
		// TODO
	}
    
    
}

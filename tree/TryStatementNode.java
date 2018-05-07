package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class TryStatementNode implements Node {
	
    public BlockNode block;
    // these two are same size;
    public ArrayList<ParamNode> catchParams;
    public ArrayList<BlockNode> catchBlocks;
    
    public BlockNode finallyPart; // optional

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		block.resolveNames(c);
		for (int i = 0; i < catchParams.size(); i++) {
			catchParams.get(i).resolveNames(c);
			catchBlocks.get(i).resolveNames(c);
		}
		if (finallyPart != null) {
			finallyPart.resolveNames(c);
		}
	}
    
    
}

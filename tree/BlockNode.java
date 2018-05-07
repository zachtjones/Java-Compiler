package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class BlockNode implements Node {
    public ArrayList<BlockStatementNode> statements;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// pass down
		for (BlockStatementNode b : statements) {
			b.resolveNames(c);
		}
	}
}

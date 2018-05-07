package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class ConstructorNode implements Node {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;

    public String name;

    public ArrayList<ParamNode> params; // empty at worst
    public ArrayList<NameNode> throwsList; // could be null

    // constructor call - either this(...) or super(...)
    //  could be null
    //public ConstructorCallNode cc;
    
    // the statements that make up the rest of the block
    public ArrayList<BlockStatementNode> code = new ArrayList<>();

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		for (ParamNode p : params) {
			p.resolveNames(c);
		}
		if (throwsList != null) {
			for (NameNode n : throwsList) {
				n.resolveNames(c);
			}
		}
		for (BlockStatementNode b : code) {
			b.resolveNames(c);
		}
	}
}

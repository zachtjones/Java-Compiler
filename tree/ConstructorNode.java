package tree;

import java.util.ArrayList;

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
}

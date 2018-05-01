package tree;

import java.util.ArrayList;

public class TryStatementNode implements Node {
    public BlockNode block;
    // these two are same size;
    public ArrayList<ParamNode> catchParams;
    public ArrayList<BlockNode> catchBlocks;
    public BlockNode finallyPart; // optional
}

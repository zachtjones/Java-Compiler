package tree;

import java.util.ArrayList;

public class SwitchStatementNode implements Node {
    public ExpressionNode expression;
    // these next two are same length
    public ArrayList<SwitchLabelNode> labels;
    public ArrayList<ArrayList<BlockStatementNode> > statements;
}

package tree;

import java.util.ArrayList;

public class ForStatementNode implements Node {
    // these 3 are all optional, so could be null
    public ForInitNode init;
    public ExpressionNode condition;
    public ArrayList<StatementExprNode> update;
    // the block of code
    public StatementNode block;
}

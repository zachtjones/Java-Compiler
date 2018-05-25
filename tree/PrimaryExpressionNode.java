package tree;

import java.util.ArrayList;

public class PrimaryExpressionNode implements ExpressionNode {
    public ExpressionNode prefix;
    public ArrayList<ExpressionNode> suffixes = new ArrayList<ExpressionNode>();
}

package tree;

import java.util.ArrayList;

/** Chain of ^ of the operands (not short-circuiting, aka bitwise or also)*/
public class ExclusiveOrExpressionNode implements ExpressionNode {
    public ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
}

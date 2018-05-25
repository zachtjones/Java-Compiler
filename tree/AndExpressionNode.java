package tree;

import java.util.ArrayList;

/** Chain of & of the operands (not short-circuiting, aka bitwise)*/
public class AndExpressionNode implements ExpressionNode {
    public ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
}

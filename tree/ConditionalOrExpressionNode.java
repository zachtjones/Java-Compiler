package tree;

import java.util.ArrayList;

/** Chain of || of the operands */
public class ConditionalOrExpressionNode implements ExpressionNode {
    public ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
}

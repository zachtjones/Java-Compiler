package tree;

import java.util.ArrayList;

/** Chain of && of the operands */
public class ConditionalAndExpressionNode implements ExpressionNode {
    public ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
}

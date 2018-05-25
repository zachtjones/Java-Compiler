package tree;

/** condition ? truePart : falsePart */
public class ConditionalExpressionNode implements ExpressionNode {
    public ExpressionNode condition;
    public ExpressionNode truePart;
    public ExpressionNode falsePart;

}

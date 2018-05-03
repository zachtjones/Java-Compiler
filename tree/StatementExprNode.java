package tree;

public class StatementExprNode implements Node {
    public boolean isPreIncrement;
    public boolean isPreDecrement;
    public boolean isPostfixExpression;
    public boolean isAssignment;

    // this holds the expression of type from above
    public ExpressionNode expression;

}

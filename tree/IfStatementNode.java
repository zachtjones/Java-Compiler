package tree;

public class IfStatementNode implements Node {
    public ExpressionNode expression;
    public StatementNode statement;
    public StatementNode elsePart;
}

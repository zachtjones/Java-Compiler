package tree;

/** (type[]...)expr */
public class ObjectCastExpressionNode implements ExpressionNode {
    public ExpressionNode expr;
    public NameNode type;
    public int arrayDims = 0;

}

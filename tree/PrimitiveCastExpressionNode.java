package tree;

/** (type[]...)expr */
public class PrimitiveCastExpressionNode implements ExpressionNode {
    public ExpressionNode expr;
    public PrimitiveTypeNode type;
    public int arrayDims = 0;

}

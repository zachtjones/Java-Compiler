package tree;

/** (type[]...)expr */
public class PrimitiveCastExpressionNode implements Expression {
    public Expression expr;
    public PrimitiveTypeNode type;
    public int arrayDims = 0;

}

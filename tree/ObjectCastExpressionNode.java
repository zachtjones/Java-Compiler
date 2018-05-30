package tree;

/** (type[]...)expr */
public class ObjectCastExpressionNode implements Expression {
    public Expression expr;
    public NameNode type;
    public int arrayDims = 0;

}

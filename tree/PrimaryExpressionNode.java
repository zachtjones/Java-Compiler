package tree;

import java.util.ArrayList;

public class PrimaryExpressionNode implements Expression {
    public Expression prefix;
    public ArrayList<Expression> suffixes = new ArrayList<Expression>();
}

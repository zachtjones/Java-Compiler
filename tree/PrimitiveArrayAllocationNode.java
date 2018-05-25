package tree;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class PrimitiveArrayAllocationNode implements ExpressionNode {
    public PrimitiveTypeNode type;
    public ArrayList<ExpressionNode> expressions; // never empty list, but can have null in the list
}

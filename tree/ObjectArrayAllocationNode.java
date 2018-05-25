package tree;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class ObjectArrayAllocationNode implements ExpressionNode {
    public NameNode type;
    public ArrayList<ExpressionNode> expressions; // never empty list, but can have null in the list
}

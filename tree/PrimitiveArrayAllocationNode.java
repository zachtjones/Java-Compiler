package tree;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class PrimitiveArrayAllocationNode implements Expression {
    public PrimitiveTypeNode type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
}

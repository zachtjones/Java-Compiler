package tree;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class ObjectArrayAllocationNode implements Expression {
    public NameNode type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
}

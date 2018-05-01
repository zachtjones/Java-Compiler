package tree;

import java.util.ArrayList;

public class VariableInitializerNode implements Node {
    // this is if you do { VariableInitializerNodes }
    // empty otherwise
    public ArrayList<VariableInitializerNode> nextLevel = new ArrayList<>();

    // this is just a normal expression
    public ExpressionNode e;
}

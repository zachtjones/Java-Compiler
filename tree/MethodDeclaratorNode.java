package tree;

import java.util.ArrayList;

public class MethodDeclaratorNode implements Node {
    public String name;
    public ArrayList<ParamNode> params;
    public int arrayDims; // 0 for non array
    // arrayDims is for the return type
}

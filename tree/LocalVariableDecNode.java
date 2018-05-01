package tree;

import java.util.ArrayList;

public class LocalVariableDecNode implements Node {
    public TypeNode type;
    public ArrayList<VariableDecNode> declarators;
}

package tree;

import java.util.ArrayList;

public class NameNode implements Node {
    public String primaryName;
    public NameNode extendsNode;
    public ArrayList<NameNode> generics;
    public NameNode secondaryName;
}

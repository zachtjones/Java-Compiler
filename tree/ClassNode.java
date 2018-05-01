package tree;

import java.util.ArrayList;

/** This represents a class in the tree
* @author Zach Jones
*/
public class ClassNode implements Node {
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isPublic;
    public String name;
    public NameNode superclass;
    public ArrayList<NameNode> interfaces;
    public ArrayList<ClassBodyNode> body = new ArrayList<>();
    public ArrayList<NameNode> typeParams;
}
